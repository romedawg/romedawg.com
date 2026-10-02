# AWS VPC Internals: How Traffic Flows

Understanding Amazon Virtual Private Cloud (VPC) at a surface level usually involves subnets, route tables, and gateways. However, understanding what happens under the hood—from the moment a packet leaves an EC2 instance to when it reaches its destination—requires a deeper dive into AWS network virtualization architecture.

---

## 1. The Core Abstractions: Software-Defined Networking (SDN)

AWS VPC is fundamentally a **Software-Defined Network (SDN)** built on top of physical data center hardware (the substrate network). 

When you configure a VPC:
* **No physical switches or routers are created.**
* **Virtual Private Clouds and Subnets** are logical abstractions implemented across distributed systems.
* **IP Addresses** do not exist as physical network ports; they are mapped dynamically within AWS's underlying SDN plane (the Mapping Service).

---

## 2. Elastic Network Interfaces (ENIs) & Virtualization

Every EC2 instance connects to a VPC via an **Elastic Network Interface (ENI)**.

### What is an ENI?
An ENI is a virtual network interface card (vNIC) bound to a specific hypervisor/host. It contains properties such as:
* Primary & Secondary Private IPv4 Addresses
* IPv6 Addresses
* MAC Address
* Security Group Associations
* Source/Destination Check Flag

### The Hypervisor Layer (Nitro Systems)
On modern AWS hardware (AWS Nitro System), network encapsulation and routing bypass the CPU hypervisor completely:
1. **Nitro Card for VPC**: A dedicated ASIC/FPGA hardware card offloads networking tasks.
2. **Encapsulation**: Packets leaving the virtual machine are encapsulated into AWS's custom substrate protocol (Geneve/VXLAN variant).
3. **Encapsulation Headers**: Substrate headers map the *overlay* network (VPC IP: 10.0.1.15) to the *underlay* network (Physical Host IP).

---

## 3. How Traffic Flows: Step-by-Step

### Scenario 1: Instance to Instance (Same Subnet, Same Host)
1. Instance A transmits an Ethernet frame to its ENI.
2. The Nitro Card intercepting the virtual interface identifies that both instances reside on the same physical server.
3. The frame is directly transferred in memory to Instance B without touching the physical fabric.

### Scenario 2: Instance to Instance (Across Availability Zones)
1. **Packet Generation**: Instance A (10.0.1.10) sends a packet to Instance B (10.0.2.20).
2. **Lookup in Mapping Service**: The local Nitro Card queries the internal, high-speed mapping control plane to find the physical host IP currently holding ENI B.
3. **Encapsulation**: The Nitro Card wraps the original packet into a substrate packet.
4. **Physical Fabric Routing**: The packet traverses the physical Clos network in the AWS Data Center.
5. **Decapsulation**: The receiving Nitro host decapsulates the substrate header and injects the original packet into Instance B's virtual interface.

---

## 4. Security Group vs. NACL Evaluation Order

Although Security Groups feel like instance-level firewalls and NACLs feel like subnet-level firewalls, **both are enforced by the underlying hypervisor/Nitro card**.

| Feature | Security Group (SG) | Network Access Control List (NACL) |
| :--- | :--- | :--- |
| **Enforcement Point** | Nitro Card / Hypervisor | Nitro Card / Hypervisor |
| **State Tracking** | **Stateful**: Outbound response allowed automatically. | **Stateless**: Inbound and outbound rules evaluated independently. |
| **Evaluation Order** | Evaluated after NACLs on ingress; evaluated before NACLs on egress. | Evaluated first on ingress; evaluated after SGs on egress. |
| **Rule Matching** | All rules evaluated (Allow-only). | Evaluated sequentially by rule number (Allow & Deny). |

### Inbound Flow Sequence:
`Substrate Packet Arrival` $\rightarrow$ `Decapsulation` $\rightarrow$ **NACL Evaluation** $\rightarrow$ **Security Group Evaluation** $\rightarrow$ `Virtual Machine Interface`

---

## 5. Internet Gateways (IGW) vs. NAT Gateways

### Internet Gateway (IGW)
An IGW is **not** a single router or physical bottleneck. It is a distributed, horizontally scaled, highly available software service.
* Performs 1-to-1 **Stateless Network Address Translation (1:1 NAT)** between Public IPs and Private IPs.
* Maps outgoing traffic from private IP space to assigned public Elastic IP (EIP) before sending it to the Internet backbone.

### NAT Gateway
Unlike the IGW, a NAT Gateway performs **Managed Stateful Network Address Translation (SNAT)**:
* Operates in a public subnet with its own Elastic IP.
* Translates multiple private IP sources into a single outbound IP address using different ephemeral ports.
* Fully managed scale, capable of handling up to 100 Gbps per gateway.

---

## 6. AWS PrivateLink and Gateway Endpoints

To avoid traversing the public internet when reaching AWS services (e.g., S3, DynamoDB, SQS):

1. **Gateway Endpoints (S3 & DynamoDB)**:
   * Modifies the VPC Route Table directly.
   * Traffic to S3 prefix lists routes internally to the service using AWS internal fabric routing without using public IP addresses or NATs.
   * **Cost**: Free.

2. **Interface Endpoints (AWS PrivateLink)**:
   * Deploys an ENI inside your subnet with a local private IP.
   * Uses DNS endpoints to forward traffic directly over AWS fabric to the target service or third-party service provider VPC.
   * **Cost**: Charged per hour and per GB processed.

---

## Summary Checklist for AWS VPC Networking

1. **VPC Routing is Distributed**: There is no central default gateway router in a VPC; routing decisions happen right at the Nitro/ENI level on the host host.
2. **NACLs and SGs are evaluated on host software/hardware**: Subnet NACLs don't block traffic *at the edge of the subnet*, but on the host physical server.
3. **No Packet Broadcasts**: VPC SDN does not support standard layer-2 ARP/IP broadcasting; mapping is resolved through control-plane lookups.