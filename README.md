# 🐵 The Curious SRE (romedawg.com)

> Welcome! This is my personal corner of the web where I keep track of technologies, test out new things, document engineering runbooks, experiment with modern architectures, and explore passions across tech, cooking, and the outdoors.

Whether it's diving into distributed microservices and database migrations, dialing in the perfect single-origin pour-over coffee, perfecting cold-water swimming sighting techniques, or tuning splitboard gear for backcountry powder—this site serves as a live laboratory, notebook, and blog.

---

## 🌟 Features

- **Clean 3-Column Layout**: Inspired by [some-natalie.dev](https://some-natalie.dev/container-escapes-ptrace/), featuring a sticky left navigation bar, center content stream, and a right sidebar with *Recently Updated* posts and topic pills.
- **Interactive Section Navigator**: Inspired by [greennode.ai](https://greennode.ai/tutorial/argocd-multi-cluster-hub-spoke-self-service), articles feature a sticky left **"ON THIS PAGE"** Table of Contents with automatic heading extraction (`<h2>`/`<h3>`), smooth click-to-scroll, and real-time **ScrollSpy**.
- **Ergonomic Markdown Studio (`/editor.html`)**:
  - Live split-view editing with instant markdown rendering.
  - SRE runbook, engineering deep-dive, and essay starter templates.
  - Automatic `localStorage` draft backup to prevent data loss.
  - Local `.md` file import and export.
  - One-click publishing to the Spring Boot backend.
- **Categorized Content**: Dedicated feeds for **Articles**, **Recipes**, **Swim**, **Snowboarding**, and **About**.
- **RESTful Backend**: High-performance Java 21 & Spring Boot 3 API with JSON-backed persistence.

---

## 🛠️ Tech Stack

- **Backend**: Java 21, Spring Boot 3.3.4, Spring Web, Jackson JSON
- **Build Tool**: Gradle 8.x (with `./gradlew` wrapper)
- **Frontend**: Vanilla HTML5, Modern CSS3 (Grid/Flexbox, CSS variables), ES6+ JavaScript, [Marked.js](https://marked.js.org/) for Markdown parsing
- **Data Storage**: JSON-based file store (`src/main/resources/data/posts.json`)

---

## 📋 Prerequisites

- **Java Development Kit (JDK)**: Java 21 or newer installed (`java -version`)
- **Git**

---

## 🚀 How to Build, Run & Deploy

### 1. Local Development (Instant Run)

To run the application locally using Gradle:

```bash
# Clone the repository
git clone https://gitlab.com/romedawg-group/romedawg.com.git
cd blog-site

# Run via Gradle bootRun
./gradlew bootRun
```

Once started, open your browser and navigate to:
- **Homepage**: [http://localhost:8080](http://localhost:8080)
- **Markdown Studio**: [http://localhost:8080/editor.html](http://localhost:8080/editor.html)

---

### 2. Building the Production JAR

To compile and package a standalone executable JAR:

```bash
# Build the project and run tests
./gradlew clean build

# The output executable JAR is generated at:
# build/libs/blog-site-1.0.0.jar
```

---

### 3. Running the Standalone JAR

Execute the built JAR directly with Java:

```bash
java -jar build/libs/blog-site-1.0.0.jar
```

#### Custom Port Configuration
By default, the server runs on port `8080`. To override the port:

```bash
# Using an environment variable
SERVER_PORT=9090 java -jar build/libs/blog-site-1.0.0.jar

# Or using a JVM system property
java -Dserver.port=9090 -jar build/libs/blog-site-1.0.0.jar
```

---

### 4. Deploying as a Background Service

#### Option A: Running with `nohup` (Simple Server Deployment)
```bash
nohup java -jar build/libs/blog-site-1.0.0.jar > app.log 2>&1 &
echo $! > app.pid
```
To stop the server:
```bash
kill $(cat app.pid)
```

#### Option B: Systemd Service (Linux / EC2 / VM)
Create `/etc/systemd/system/blog-site.service`:

```ini
[Unit]
Description=The Curious SRE Blog Service
After=network.target

[Service]
User=ubuntu
WorkingDirectory=/opt/blog-site
ExecStart=/usr/bin/java -jar /opt/blog-site/build/libs/blog-site-1.0.0.jar
SuccessExitStatus=143
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

Enable and start:
```bash
sudo systemctl daemon-reload
sudo systemctl enable blog-site
sudo systemctl start blog-site
```

#### Option C: Container Deployment (Docker)
Create a `Dockerfile`:

```dockerfile
# Build Stage
FROM gradle:8.10-jdk21 AS build
WORKDIR /app
COPY . .
RUN ./gradlew build --no-daemon -x test

# Run Stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/blog-site-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build and run:
```bash
docker build -t blog-site:latest .
docker run -d -p 8080:8080 --name blog-site blog-site:latest
```

---

## 📁 Project Structure

```text
blog-site/
├── build.gradle                              # Gradle build configuration
├── settings.gradle                           # Project settings
├── gradlew / gradlew.bat                     # Gradle wrapper scripts
├── src/
│   ├── main/
│   │   ├── java/com/curiousape/blog/
│   │   │   ├── BlogApplication.java          # Spring Boot main entrypoint
│   │   │   ├── controller/
│   │   │   │   └── PostController.java       # REST API endpoints (/api/posts)
│   │   │   ├── model/
│   │   │   │   └── Post.java                 # Post data model
│   │   │   └── service/
│   │   │       └── PostService.java          # Business logic & JSON file persistence
│   │   └── resources/
│   │       ├── application.properties        # Application configuration
│   │       ├── data/
│   │       │   └── posts.json                # Seed / saved blog posts
│   │       └── static/
│   │           ├── index.html                # 3-column main blog layout
│   │           ├── styles.css                # Site & article reader styles
│   │           ├── app.js                    # Client-side router, TOC ScrollSpy & API client
│   │           ├── editor.html               # Markdown Studio editor
│   │           ├── editor.css                # Split-screen editor styling
│   │           ├── editor.js                 # Editor toolbar, shortcuts, templates, draft store
│   │           └── images/
│   │               └── monkey.jpg            # Mascot avatar
```

---

## 📡 REST API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/posts` | List all posts (optional query: `?category=Swim` or `?tag=SRE`) |
| `GET` | `/api/posts/{id}` | Fetch a single post by ID or slug |
| `POST` | `/api/posts` | Create a new blog post |
| `POST` | `/api/posts/{id}/like` | Increment like counter for a post |
| `DELETE` | `/api/posts/{id}` | Delete a post |

---

## 📝 Authoring New Posts

1. Navigate to **[http://localhost:8080/editor.html](http://localhost:8080/editor.html)**.
2. Pick a template from the **📋 Templates** dropdown or start writing Markdown from scratch.
3. Use the toolbar or shortcuts (`Ctrl+B`, `Ctrl+I`, `Ctrl+K`, etc.).
4. Add Title, Category (`Articles`, `Recipes`, `Swim`, `Snowboarding`, etc.), and Tags.
5. Click **"Publish Story 🚀"** to save to the backend.

---

## Postgres permissions
```
CREATE DATABASE blog_db;
\c blog_db 

CREATE USER romedawg WITH PASSWORD 'password';

GRANT CONNECT ON DATABASE romedawg TO romedawg;

GRANT USAGE, CREATE ON SCHEMA public TO romedawg;

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO romedawg;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO romedawg;
```

## 📄 License

MIT License. Feel free to explore, fork, and adapt for your own experiments!
