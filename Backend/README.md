# 📄 Resume Screener API

A **production-ready Spring Boot REST API** that leverages **AI/NLP (HuggingFace Inference API)** to analyze resumes against job descriptions, generate **match scores**, and identify **skill gaps**.

Built with a focus on **scalable backend architecture, clean code practices, and real-world engineering standards**.

---

##  Key Highlights

- RESTful API design following **industry best practices**
- AI-powered resume analysis using **HuggingFace NLP models**
- **PDF parsing & text extraction pipeline**
- **Semantic skill matching & gap identification**
- **Session-based tracking using sessionId**
- **Scheduled background jobs (Spring Scheduler)**
- Persistent storage using **MySQL**
- Clean architecture (**Controller → Service → Repository**)
- Structured API responses using DTOs
- Integrated API documentation (Swagger)

---

## 🛠 Tech Stack

- **Backend:** Spring Boot (Java)
- **AI/NLP:** HuggingFace Inference API
- **Database:** MySQL
- **API Docs:** Swagger / OpenAPI
- **Testing:** Postman
- **Scheduler:** Spring Scheduler

---

## ⚙️ System Workflow

1. Upload Resume (PDF)  
2. Extract text from resume  
3. Input Job Description  
4. Generate unique sessionId  
5. Send data to HuggingFace API  
6. Compute match score using NLP  
7. Extract:
   - Matched Skills
   - Missing Skills  
8. Store results with sessionId  
9. Return structured API response  

---

## 📡 API Endpoints

### 🔹 Analyze Resume

POST `/resume/screen`

**Request:**
- Multipart file (PDF Resume)
- Job Description (text)
  
### 🔹 Get History

GET `/resume/history`

**Request:**
- sessionId

**Response:**
```json
{
  "id": 3,
  "sessionId": "6998aab0-a83f-49a2-bfe0-10fba1d6b4d5",
  "matchScore": 55.7,
  "matchedSkills": [
    "Java",
    "Spring Boot",
    "REST API"
  ],
  "missingSkills": [
    "Angular",
    "Microservices"
  ],
  "verdict": "PARTIAL MATCH",
  "jobDescription": "Looking for a Java Full Stack Developer with experience in Spring Boot, REST APIs, Microservices, MySQL, Angular. Knowledge of Docker and Kafka.",
  "resumeFileName": "Sarthak.pdf",
  "screenAt": "2026-04-16T12:30:35.5913893"
}
```
