# Comprehensive Guide to Packet Routing in an AWS VPC

This document details how network packets traverse an AWS Virtual Private Cloud (VPC) for both inbound traffic (from an external user to an EC2 instance or ECS task) and outbound traffic (from those services back to the internet or internal VPC destinations).

---

## Part 1: Inbound Traffic Flow (User to EC2 / ECS)

When an end user accesses a service hosted on an EC2 instance or an ECS task running on EC2/Fargate within a VPC, traffic flows through several networking layers.

```
+-----------------------------------------------------------------------------------+
|                                     Internet                                      |
+-----------------------------------------------------------------------------------+
                                          |
                                          v
                               +--------------------+
                               |    Internet Gateway|
                               +--------------------+
                                          |
                                          v
                               +--------------------+
                               | Public Subnet Route|
                               |       Table        |
                               +--------------------+
                                          |
                                          v
                               +--------------------+
                               | Application Load   |
                               |    Balancer (ALB)  |
                               +--------------------+
                                          |
                                          v
                               +--------------------+
                               | Private Subnet     |
                               |    Route Table     |
                               +--------------------+
                                          |
                                          v
                               +--------------------+
                               | Target (EC2 / ECS) |
                               +--------------------+
```

### Detailed Inbound Steps

1. **DNS Resolution:**
   * The user enters a URL (e.g., `app.example.com`).
   * DNS resolves the domain name to the public IP address of the Internet Gateway / Load Balancer.

2. **Edge Entry (Internet Gateway - IGW):**
   * Packets enter the AWS network at the **Internet Gateway (IGW)** attached to the VPC.
   * The IGW performs 1:1 Network Address Translation (NAT) if traffic is directed toward a public IP assigned to an ENI (Elastic Network Interface).

3. **Subnet Route Table Evaluation:**
   * The packet reaches the **Public Subnet**.
   * The subnet’s **Route Table** evaluates the destination IP address to determine local routing within the VPC CIDR block.

4. **Network Access Control Lists (NACLs) – Subnet Boundary:**
   * Before entering the subnet, the packet is evaluated against the **Stateless Inbound NACL rules** associated with the public subnet.
   * If allowed, the packet proceeds.

5. **Load Balancer / Ingress Controller (Optional but Standard):**
   * In production architectures, traffic typically terminates at an **Application Load Balancer (ALB)** or **Network Load Balancer (NLB)**.
   * **ALB (Layer 7):** Terminates TLS, inspects HTTP/HTTPS headers, and proxies the request to a backend target group across private subnets.
   * **NLB (Layer 4):** Pass-through routing based on IP/Port with low latency, preserving client IP addresses.

6. **Private Subnet & Security Group Evaluation:**
   * Traffic routes from the Load Balancer to the private subnet where the target EC2 or ECS task resides.
   * **Private Subnet NACL:** Evaluates inbound rules for the private subnet.
   * **Security Group (Stateful):** Evaluates rules attached to the target’s ENI. Security groups track state; allowing inbound traffic automatically allows the response traffic outbound regardless of outbound rules.

7. **Host / Interface Delivery:**
   * The packet reaches the primary or secondary Elastic Network Interface (**ENI**) of the EC2 instance or ECS task.
   * **ECS Networking Modes:**
     * `awsvpc`: Task gets its own dedicated ENI, IP address, and security group.
     * `bridge`: Traffic maps to a host port via Docker’s port mapping on the EC2 instance.
     * `host`: Task shares the network namespace of the underlying EC2 host.

---

## Part 2: Outbound Traffic Flow

Outbound routing depends heavily on whether the destination is **Internal** (inside the VPC or connected corporate networks) or **Public** (the public internet).

---

### Scenario A: Public Outbound Traffic (Internet-Bound)

#### 1. From a Public Subnet (Direct Internet Access)
If an EC2 instance is in a public subnet and has a Public/Elastic IP assigned:

1. **Instance Request:** Instance sends a packet with destination `0.0.0.0/0`.
2. **Security Group:** Checks stateful outbound rules (default allows all egress).
3. **Subnet NACL:** Evaluates outbound rules for the public subnet.
4. **Route Table Lookup:** Matches the default route target:
   ```text
   Destination: 0.0.0.0/0  -->  Target: igw-xxxxxxxx (Internet Gateway)
   ```
5. **IGW Translation:** The IGW maps the instance’s private IP to its public Elastic IP and forwards the packet to the Internet.

#### 2. From a Private Subnet (Via NAT Gateway)
Instances or ECS tasks in private subnets cannot directly access the IGW.

```
[Private EC2/ECS Task]
       |
       | (Route: 0.0.0.0/0 -> nat-xxx)
       v
[Private Subnet Route Table]
       |
       v
[NAT Gateway (Public Subnet)]
       |
       | (Route: 0.0.0.0/0 -> igw-xxx)
       v
[Internet Gateway] ---> Internet
```

1. **Instance Request:** EC2 or ECS task sends a packet destined for external internet (`0.0.0.0/0`).
2. **Private Route Table:** Evaluates destination against routes:
   ```text
   Destination: 0.0.0.0/0  -->  Target: nat-xxxxxxxx (NAT Gateway)
   ```
3. **NAT Gateway Processing:** Located in a **Public Subnet**, the NAT Gateway receives the packet, replaces the source private IP with its assigned Elastic IP, and updates its translation table.
4. **Public Route Table:** The packet exits the NAT Gateway and uses the Public Subnet's route table:
   ```text
   Destination: 0.0.0.0/0  -->  Target: igw-xxxxxxxx
   ```
5. **IGW:** Forwards the packet to the public internet.

---

### Scenario B: Internal Outbound Traffic

Internal traffic never leaves the AWS private network backbone. Routing is determined by the target destination type.

```
                             +-----------------------+
                             | Target EC2 / ECS Task |
                             +-----------------------+
                                         |
                       +-----------------+-----------------+
                       |                                   |
                       v                                   v
             [VPC Local Route]                    [Non-Local Route]
                       |                                   |
          +------------+------------+            +---------+---------+
          |                         |            |                   |
          v                         v            v                   v
   [Same Subnet]            [Cross-Subnet]  [VPC Peering/TGW]  [VPC Endpoints]
   (Direct MAC)             (Subnet Router) (Peered VPC/On-Prem)(S3, DynamoDB, API)
```

#### 1. Intra-VPC Traffic (Same VPC, Subnet-to-Subnet)
Every VPC route table contains an unmodifiable **Local Route**:
```text
Destination: 10.0.0.0/16 (VPC CIDR)  -->  Target: local
```

* **Same Subnet:** Packet travels directly over virtual network software switches using ARP/MAC mappings without touching explicit gateway targets.
* **Across Subnets / AZs:** Packet routes through hypervisor-level VPC routers. Security Groups and NACLs at both source and target subnets are evaluated.

#### 2. VPC to AWS Services (VPC Endpoints / PrivateLink)
To keep traffic to AWS services (e.g., S3, DynamoDB, ECR, Systems Manager) strictly on the private AWS network:

* **Gateway Endpoints (S3 & DynamoDB):**
  * Modifies the subnet route table directly.
  * Route Entry: `Destination: pl-xxxxxx (Prefix List) --> Target: vpce-xxxxxx`
  * Traffic bypasses NAT Gateways and stays fully internal.
* **Interface Endpoints (AWS PrivateLink):**
  * Creates an Elastic Network Interface (ENI) inside your private subnet with a local IP address.
  * DNS queries for the AWS service resolve to this private ENI IP address.
  * Traffic flows directly over the `local` VPC route.

#### 3. Cross-VPC & On-Premises Connectivity

* **VPC Peering:**
  * Direct network connection between two VPCs.
  * Route Table Entry: `Destination: 172.16.0.0/16 --> Target: pcx-xxxxxx`
* **Transit Gateway (TGW) / Direct Connect / Site-to-Site VPN:**
  * Used for complex hub-and-spoke models connecting multiple VPCs and on-premises corporate datacenters.
  * Route Table Entry: `Destination: 192.168.0.0/16 --> Target: tgw-xxxxxx` or `vgw-xxxxxx`

---

## Traffic Summary Matrix

| Traffic Source | Target Destination | Required Route Table Target | Intermediate Devices |
| :--- | :--- | :--- | :--- |
| **Public Subnet** | Internet (`0.0.0.0/0`) | `igw-xxxxxxxx` | Internet Gateway |
| **Private Subnet** | Internet (`0.0.0.0/0`) | `nat-xxxxxxxx` | NAT Gateway $\rightarrow$ Internet Gateway |
| **Private Subnet** | Same VPC (`10.0.0.0/16`) | `local` | VPC Virtual Router |
| **Private Subnet** | S3 / DynamoDB | `vpce-xxxxxx` (Gateway) | VPC Gateway Endpoint |
| **Private Subnet** | AWS Services (ECR, SSM) | `local` (via DNS) | VPC Interface Endpoint (PrivateLink) |
| **Private Subnet** | Peered VPC | `pcx-xxxxxx` | VPC Peering Connection |
| **Private Subnet** | On-Premises | `tgw-xxxxxx` / `vgw-xxxxxx` | Transit Gateway / Virtual Private Gateway |