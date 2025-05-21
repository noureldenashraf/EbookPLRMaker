
# Ebook Content Generator

A **Spring Boot-based REST API** for generating structured content files (TXT, HTML, DOCX) using AI-driven prompts. The backend leverages **Spring AI** for content generation, **docx4j** for DOCX creation, and **MariaDB** for persistent storage. Normalized prompts ensure consistent output across file types.

> **Note:** This project is **backend-only** and currently in **active development**.

---

## Features

- **Content Generation**: Generates content for TXT, HTML, DOCX using Spring AI.
- **File Type Support**: Creates files with normalized prompts tailored to each file type.
- **DOCX Creation**: Uses docx4j to produce richly formatted DOCX files.
- **Persistent Storage**: Stores file metadata and prompts in MariaDB.
- **REST API**: Exposes endpoints for managing files, generating content, and downloading outputs.
- **Scalable Backend**: Built with Spring Boot for robust API performance.

---

## Tech Stack

- **Backend**: Spring Boot 3.x  
- **Database**: MariaDB  
- **AI Content Generation**: Spring AI  
- **DOCX Processing**: docx4j  
- **Build Tool**: Maven  
- **Java**: 17 or later  

---

## Prerequisites

- Java 17 or later  
- MariaDB 10.5 or later  
- Maven 3.8 or later  

---

## Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/noureldenashraf/EbookPLRMaker.git
cd EbookPLRMaker
```

### 2. Update `application.properties`

Edit `src/main/resources/application.properties` with your DB credentials:

```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/YourDB
spring.datasource.username=root
spring.datasource.password=root
```

### 3. Configure Spring AI

- Obtain an API key for your AI provider (e.g., OpenAI).
- Add the key to `application.properties`:

```properties
spring.ai.provider.api-key=your-api-key
```

### 4. Build and Run

```bash
mvn clean install
mvn spring-boot:run
```

> API will be available at: `http://localhost:8080`

---

## API Endpoints

### 📂 File Management

#### ➕ Add File

**POST** `/api/files`  
**Content-Type**: `application/json`

```json
{
  "name": "example",
  "prompt": "Generate a chapter on AI ethics.",
  "type": "DOCX",
  "folder": "Documents"
}
```

#### 📃 List Files

**GET** `/api/files`

#### ✏️ Update File

**PUT** `/api/files`  
**Content-Type**: `application/json`

```json
{
  "name": "updated-example",
  "type": "TXT",
  "folder": "Marketing",
  "prompt": "Generate a blog post on AI trends."
}
```

#### ❌ Delete File

**DELETE** `/api/files/{id}`

---

### ⚙️ Content Generation

#### 📄 Generate File

**GET** `/api/files/generate`  
**Content-Type**: `application/json`

```json
{
  "fileIds": "16"
}
```

📎 Response: Binary file (e.g., `example.docx`)

#### 🗜️ Generate ZIP

**POST** `/api/files/generate`  
**Content-Type**: `application/json`

```json
{
  "fileIds": "16,32,12"
}
```

📎 Response: Binary ZIP file

---

## 🧠 Normalized Prompts

Each file type uses a standardized prompt format for consistent AI-generated content:

- **TXT**: Plain text, optimized for readability.  
- **HTML**: Structured with semantic tags, suitable for web content.  
- **DOCX**: Formatted with headings, lists, and styles via docx4j.  

### Example Prompt for DOCX

> Generate a professional document with a title, introduction, three sections with subheadings, and a conclusion. Use bold for emphasis and include a bulleted list.

---

## 🚧 Project Status

- **Backend**: ✅ Most of the features are Complete however i'm still working on the rest of the features and fixing the bugs  
- **Frontend**: ❌ Not implemented (planned for future development).  
- **PDF Conversion**: ❌ Not supported yet.

---

## 🤝 Contributing

Contributions are welcome! To contribute:

1. Fork the repository  
2. Create a feature branch  
   ```bash
   git checkout -b feature/your-feature
   ```
3. Commit your changes  
   ```bash
   git commit -m "Add your feature"
   ```
4. Push the branch  
   ```bash
   git push origin feature/your-feature
   ```
5. Open a Pull Request

> Please include tests and update documentation as needed.

---

## 📝 License

This project is licensed under the **MIT License**. See the [LICENSE](LICENSE) file for details.

---

## 📬 Contact

For questions or support:
- Open an issue on [GitHub](https://github.com/noureldenashraf/EbookPLRMaker)
- Email: **n.ashraf956@gmail.com**
