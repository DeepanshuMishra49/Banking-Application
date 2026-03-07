# 🏦 Banking Application

<div align="center">

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Jenkins](https://img.shields.io/badge/Jenkins-D24939?style=for-the-badge&logo=jenkins&logoColor=white)
![AWS](https://img.shields.io/badge/AWS-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white)
![Prometheus](https://img.shields.io/badge/Prometheus-E6522C?style=for-the-badge&logo=prometheus&logoColor=white)
![Grafana](https://img.shields.io/badge/Grafana-F46800?style=for-the-badge&logo=grafana&logoColor=white)

**A full-stack banking application with a complete DevOps pipeline — containerized, monitored, and auto-deployed on AWS.**

</div>

---

## 📸 Screenshots

| Banking Dashboard | Account Page |
|:-:|:-:|
| ![Dashboard](screenshots/dashboard.png) | ![Account](screenshots/account.png) |

| Grafana Monitoring | Jenkins Pipeline |
|:-:|:-:|
| ![Grafana](screenshots/grafana.png) | ![Jenkins](screenshots/jenkins.png) |

---

## 🚀 Features

- 🔐 **User Authentication** — Register, Login, Logout with Spring Security
- 🏦 **Account Management** — Create and manage savings accounts
- 💸 **Transactions** — Deposit, Withdraw, and Transfer between accounts
- 📜 **Transaction History** — Full history with date, type, amount & description
- 📊 **Real-time Monitoring** — JVM metrics via Prometheus + Grafana dashboard
- 🔄 **Auto CI/CD** — Push to GitHub → Jenkins auto-builds and deploys

---

## 🏗️ Architecture

```
Developer (Push Code)
        │
        ▼
   GitHub Repo
        │
        ▼ (Webhook Trigger)
  Jenkins Master (EC2)
        │
        ▼
  Jenkins Agent (EC2 + Elastic IP)
        │
    ┌───┴────────────────────┐
    │                        │
 Maven Build            Docker Build
    │                        │
    └───────────┬────────────┘
                │
                ▼
          DockerHub Push
                │
                ▼
         Docker Compose (Deploy)
         ┌──────────────────────┐
         │  Banking App :8088   │
         │  MySQL       :3306   │
         │  Prometheus  :9090   │
         │  Grafana     :3000   │
         └──────────────────────┘
                │
                ▼
    Prometheus scrapes /actuator/prometheus
                │
                ▼
    Grafana JVM Micrometer Dashboard
```

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Spring Boot 3, Spring Security, Spring Data JPA |
| Frontend | Thymeleaf, HTML/CSS |
| Database | MySQL 8 |
| Containerization | Docker, Docker Compose |
| CI/CD | Jenkins (Master-Agent on AWS EC2) |
| Monitoring | Prometheus, Grafana (JVM Micrometer Dashboard) |
| Cloud | AWS EC2 (t2.micro), Elastic IP |
| Build Tool | Maven |

---

## ⚙️ Jenkins CI/CD Pipeline

```groovy
pipeline {
    agent { label 'agent' }
    stages {
        stage('Code')        { /* Git Checkout */ }
        stage('Build Maven') { /* mvn clean package */ }
        stage('Build')       { /* Docker image build */ }
        stage('Image Push')  { /* Push to DockerHub */ }
        stage('Deploy')      { /* docker-compose up */ }
    }
}
```

**Pipeline Flow:**

```
Code Checkout → Maven Build (15s) → Docker Build (1m 25s) → Image Push (14s) → Deploy (47s)
                                                              Total: ~3 mins
```

---

## 📦 Docker Services

```yaml
services:
  java-app:    # Spring Boot App  → port 8088
  mysql:       # MySQL Database   → port 3306
  prometheus:  # Metrics Scraper  → port 9090
  grafana:     # Visualization    → port 3000
```

---

## 🚦 Getting Started

### Prerequisites

- Docker & Docker Compose installed
- Java 17+
- Maven 3.x

### Run Locally

```bash
# 1. Clone the repo
git clone https://github.com/DeepanshuMishra49/Banking-Application.git
cd Banking-Application

# 2. Build the Docker image
docker build -t banking-app-image:latest .

# 3. Start all services
docker-compose up -d

# 4. Access the app
open http://localhost:8088
```

### Access Services

| Service | URL | Credentials |
|---------|-----|-------------|
| Banking App | http://localhost:8088 | Register a new account |
| Prometheus | http://localhost:9090 | — |
| Grafana | http://localhost:3000 | admin / admin |

---

## 📊 Monitoring Setup

### Prometheus
Scrapes metrics every **15 seconds** from Spring Boot Actuator:
```
http://java-app:8088/actuator/prometheus
```

### Grafana Dashboard
Import **JVM Micrometer Dashboard (ID: 4701)** from Grafana Labs.

Metrics tracked:
- ☕ JVM Heap & Non-Heap Memory
- 🖥️ CPU Usage (System + Process)
- 🌐 HTTP Request Rate, Duration & Errors
- 🧵 Thread States
- ♻️ Garbage Collection Pause Times
- 📁 File Descriptors

---

## ☁️ AWS Infrastructure

```
┌─────────────────────────────────────────┐
│              AWS EC2 (Mumbai)           │
│                                         │
│  ┌─────────────────┐  ┌──────────────┐  │
│  │  Jenkins MASTER │  │Jenkins AGENT │  │
│  │   t2.micro      │  │  t2.micro    │  │
│  │                 │  │  Elastic IP  │  │
│  └─────────────────┘  │  Swap File   │  │
│                        └──────────────┘  │
└─────────────────────────────────────────┘
```

**Key configurations:**
- **Elastic IP** on Agent node — IP never changes, no manual reconfiguration needed
- **Swap File** on t2.micro — extends available memory for Docker builds

---

## 📁 Project Structure

```
Banking-Application/
├── src/
│   ├── main/
│   │   ├── java/com/example/Banking_Application/
│   │   │   ├── config/          # Security config
│   │   │   ├── controller/      # MVC Controllers
│   │   │   ├── model/           # JPA Entities
│   │   │   ├── repository/      # Spring Data Repos
│   │   │   └── service/         # Business Logic
│   │   └── resources/
│   │       ├── templates/       # Thymeleaf views
│   │       └── application.properties
├── Dockerfile
├── docker-compose.yml
├── prometheus.yml
└── Jenkinsfile
```

---

## 🔧 Key Configuration

### application.properties (Monitoring)
```properties
management.endpoints.web.exposure.include=prometheus,health
management.endpoint.prometheus.enabled=true
management.metrics.tags.application=${spring.application.name}
```

### prometheus.yml
```yaml
scrape_configs:
  - job_name: 'Banking-Application'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['java-app:8088']
```

---

## 🧠 What I Learned

- Docker networking between containers using named networks
- Spring Security configuration for Actuator endpoint access
- Jenkins Master-Agent setup on separate EC2 instances
- AWS Elastic IP for stable agent configuration
- Grafana datasource UID and variable configuration
- Swap file setup for memory-constrained environments
- Writing a complete declarative Jenkinsfile pipeline

---

## 👨‍💻 Author

**Deepanshu Mishra**

[![GitHub](https://img.shields.io/badge/GitHub-DeepanshuMishra49-181717?style=flat&logo=github)](https://github.com/DeepanshuMishra49)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Connect-0A66C2?style=flat&logo=linkedin)](https://linkedin.com/in/your-profile)

---

<div align="center">
  <sub>Built with ❤️ and a lot of debugging 🐛</sub>
</div>
