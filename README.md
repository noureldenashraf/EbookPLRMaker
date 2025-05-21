Ebook Content Generator
A Spring Boot-based REST API for generating structured content files (TXT, HTML, DOCX) using AI-driven prompts. The backend leverages Spring AI for content generation, docx4j for DOCX creation, and MariaDB for persistent storage. Normalized prompts ensure consistent output across file types. This project is backend-only and currently in active development.
Features

Content Generation: Generates content for TXT, HTML, DOCX using Spring AI.
File Type Support: Creates files with normalized prompts tailored to each file type.
DOCX Creation: Uses docx4j to produce richly formatted DOCX files.
Persistent Storage: Stores file metadata and prompts in MariaDB.
REST API: Exposes endpoints for managing files, generating content, and downloading outputs.
Scalable Backend: Built with Spring Boot for robust API performance.

Tech Stack

Backend: Spring Boot 3.x
Database: MariaDB
AI Content Generation: Spring AI
DOCX Processing: docx4j
Build Tool: Maven
Java: 17 or later

Prerequisites

Java 17 or later
MariaDB 10.5 or later
Maven 3.8 or later

Setup Instructions

Clone the Repository:
git clone https://github.com/noureldenashraf/EbookPLRMaker.git
cd EbookPLRMaker



Update src/main/resources/application.properties with your database credentials:spring.datasource.url=jdbc:mariadb://localhost:3306/YourDB
spring.datasource.username=root
spring.datasource.password=root



Configure Spring AI:

Obtain an API key for your AI provider (e.g., OpenAI, if used by Spring AI).
Add the key to application.properties:spring.ai.provider.api-key=your-api-key




Build and Run:
mvn clean install
mvn spring-boot:run

The API will be available at http://localhost:8080.

API Endpoints
File Management

Add File: Create a new file entry with a prompt.POST /api/files
Content-Type: application/json
{
  "name": "example",
  "prompt": "Generate a chapter on AI ethics.",
  "type": "DOCX",
  "folder": "Documents"
  
}


List Files: Retrieve all files.GET /api/files


Update File: Modify an existing file.PUT /api/files
Content-Type: application/json
{
  "name": "updated-example",
  "type": "TXT",
  "folder": "Marketing",
  "prompt": "Generate a blog post on AI trends."
}


Delete File: Remove a file.DELETE /api/files/{id}



Content Generation

Generate File: Generate content for a specific file and download it.GET /api/files/generate
Content-Type: application/json

{
  "fileIds": "16"
}
Response: Binary file (e.g., example.docx)

Generate ZIP: Generate and download multiple files as a ZIP.POST /api/files/generate
Content-Type: application/json
{
  "fileIds": "16,32,12"
}
Response: Binary ZIP file


Normalized Prompts
Each file type uses a standardized prompt format to ensure consistent AI-generated content:

TXT: Plain text, optimized for readability.
HTML: Structured with semantic tags, suitable for web content.
DOCX: Formatted with headings, lists, and styles via docx4j.


Example prompt for DOCX:
Generate a professional document with a title, introduction, three sections with subheadings, and a conclusion. Use bold for emphasis and include a bulleted list.

Project Status

Backend: Most of the things are Complete, with fully functional REST API, content generation, and file management.
Frontend: Not implemented (planned for future development).
PDF Conversion: Not supported Yet.

Contributing
Contributions are welcome! To contribute:

Fork the repository.
Create a feature branch (git checkout -b feature/your-feature).
Commit your changes (git commit -m "Add your feature").
Push to the branch (git push origin feature/your-feature).
Open a pull request.

Please include tests and update documentation as needed.
License
This project is licensed under the MIT License. See the LICENSE file for details.
Contact
For questions or support, open an issue on GitHub or contact n.ashraf956@gmail.com.
