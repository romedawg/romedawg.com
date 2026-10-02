# ArgoCD: App-of-Apps vs. ApplicationSets


---

Both the **App-of-Apps** pattern and **ApplicationSets** solve the same fundamental GitOps challenge: you have multiple applications to manage in Argo CD and you want to avoid manually creating and maintaining individual `Application` resources.

However, they solve this problem differently, and that distinction becomes critical as your system grows. Most teams adopt one pattern early and discover the other later—usually when their initial choice starts introducing friction.

---

## 1. App-of-Apps: Applications Managing Applications

The **App-of-Apps** pattern uses a single "parent" Argo CD `Application` that points to a Git directory containing other `Application` manifests ("child" apps). 

When Argo CD syncs the parent application, it creates the child `Application` resources, which in turn sync their own application resources (Deployments, Services, ConfigMaps, etc.).

```
                         ┌─────────────────────┐
                         │ Parent Application  │
                         └──────────┬──────────┘
                                    │
            ┌───────────────────────┼───────────────────────┐
            ▼                       ▼                       ▼
┌───────────────────────┐ ┌───────────────────┐ ┌───────────────────────┐
│ Child Application A   │ │ Child Application │ │ Child Application C   │
└───────────────────────┘ └───────────────────┘ └───────────────────────┘
```

### Git Directory Structure
```text
gitops/
├── root-app.yaml                 # Parent Application manifest
└── apps/
    ├── app1.yaml      # Child Application
    ├── app2.yaml        # Child Application
    └── app3.yaml # Child Application
```

### Key Advantages
* **Explicit & Flexible:** Every child application manifest is fully explicit. Each can have its own sync policies, destination clusters, source paths, or chart settings.
* **Low Learning Curve:** Uses basic Kubernetes manifests without complex templating engines or abstract custom resources.
* **Minimal Friction:** Easy to implement when migrating from standalone Argo CD `Application` definitions.

---

## 2. ApplicationSets: Templated Generation at Scale

An **ApplicationSet** uses a custom controller that dynamically generates Argo CD `Application` resources using a single template paired with one or more **Generators** (e.g., Git directory generators, Git file generators, Cluster generators, or Matrix generators).

```
                         ┌─────────────────────┐
                         │   ApplicationSet    │
                         └──────────┬──────────┘
                                    │ (Generates via Template)
            ┌───────────────────────┼───────────────────────┐
            ▼                       ▼                       ▼
┌───────────────────────┐ ┌───────────────────┐ ┌───────────────────────┐
│ Generated App 1       │ │ Generated App 2   │ │ Generated App 3       │
└───────────────────────┘ └───────────────────┘ └───────────────────────┘
```

### Git Directory Structure
```text
gitops/
├── applicationsets/
│   ├── production-services.yaml   # ApplicationSet manifest
│   └── staging-services.yaml      # ApplicationSet manifest
└── services/
    ├── app1/
    │   ├── production/
    │   └── staging/
    └── app2/
        ├── production/
        └── staging/
```

### Example: Git Directory Generator
An `ApplicationSet` can watch the `services/` directory. Every subdirectory automatically becomes a managed `Application`. 
* Adding `services/app3/` triggers Argo CD to generate the `app3` `Application`.
* Removing the directory automatically deletes the corresponding `Application`.

### Key Advantages
* **Drastic Reduction in Boilerplate:** Eliminates repetitive `Application` manifest files across environments or clusters.
* **Template-Enforced Consistency:** Ensures every generated application adheres to organizational standards (standard sync options, annotations, destination rules, etc.).
* **Dynamic Scaling:** Adding a service, cluster, or environment requires minimal or zero code changes in Git.

---

## 3. Where Each Pattern Struggles

| Limitation Factor | App-of-Apps Pattern | ApplicationSet Pattern |
| :--- | :--- | :--- |
| **Maintenance Burden** | High boilerplate. Adding a service to 10 environments requires maintaining 10 individual files. Updating a setting requires touching every file. | Low boilerplate, but modifying exceptions requires `templatePatch` workarounds or Go-template logic. |
| **Structural Diversity** | Handles diverse applications effortlessly (e.g., Helm vs. Kustomize vs. raw manifests in the same folder). | Struggles when services have structurally different definitions or tooling requirements. |
| **Manual Overrides** | Allows granular adjustments, but risks drift if team practices are uncoordinated. | Rigid ownership. The `ApplicationSet` controller will overwrite direct edits made to generated `Application` manifests. |
| **Configuration Risks** | Mistakes are usually limited to a single application file. | Missing or misconfigured template variables can render empty values (e.g., empty namespace), potentially deploying resources to unintended locations across scale. |

---

## 4. RBAC and Team Ownership

* **App-of-Apps (Decentralized Autonomy):** Fits organizations where developers or service teams maintain full ownership over their application deployment specs. Teams can independently update their own child `Application` manifests in Git without platform intervention.
* **ApplicationSets (Centralized Governance):** Fits platform-centric models. The platform team owns the `ApplicationSet` definition and deployment template, while application developers only manage their app code or values within designated directories (`services/`).

---

## 5. Summary Comparison

| Feature / Criteria | App-of-Apps | ApplicationSets |
| :--- | :--- | :--- |
| **Primary Mechanism** | Parent `Application` syncing child files | Controller templating via Generators |
| **Best For** | Heterogeneous apps, small application counts (<20) | Multi-cluster, multi-environment, homogeneous microservices |
| **Configuration Style** | Explicit, file-per-application | DRY (Don't Repeat Yourself), templated |
| **Operational Overhead** | Scales linearly with application count | Stays flat regardless of application count |

---

## 6. When to Use Which?

### Use App-of-Apps when:
1. You have a relatively small number of total applications (under 20).
2. Each application has genuinely unique configurations that don't fit standard templates.
3. Teams require maximum flexibility and autonomy over their Argo CD manifest definitions.
4. You are migrating existing standalone `Application` resources with minimal initial friction.

### Use ApplicationSets when:
1. You manage a large fleet of microservices across multiple clusters, environments, or tenants.
2. The platform team needs to enforce uniform deployment practices and guardrails.
3. You rely on directory conventions or external cluster inventory tools.

---

## 7. Recommended Production Architecture: Combining Both Patterns

For mature, enterprise GitOps setups, the recommended pattern is actually a **hybrid combination**:

```
                         ┌────────────────────────────────┐
                         │      Root App-of-Apps          │
                         │   (App-of-ApplicationSets)     │
                         └───────────────┬────────────────┘
                                         │
                 ┌───────────────────────┴───────────────────────┐
                 ▼                                               ▼
    ┌──────────────────────────┐                    ┌──────────────────────────┐
    │     ApplicationSet       │                    │    Individual App        │
    │  (Homogeneous Services)  │                    │     (Edge Cases)         │
    └──────────────────────────┘                    └──────────────────────────┘
```

1. A **Root App-of-Apps** serves as the single entry point for Argo CD.
2. The Root App manages **ApplicationSets** for standard microservices (e.g., core API services deployed across environments).
3. The Root App simultaneously manages individual, explicit **Application manifests** for one-off workloads or infrastructure components (e.g., databases, ingress controllers, monitoring stacks) that do not fit standard templates.

This hybrid model gives platform teams enforced standardisation where scale is required, without sacrificing flexibility for unique workloads.