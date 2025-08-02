# 🚀 HealSync Backend - Complete API Reference

## 🌐 Base URL Pattern
Your backend uses `/v1/healsync/` prefix for most endpoints, and `/api/` for treatment plans.

---

## 📋 TREATMENT PLAN APIs (Primary Focus)

### 🎯 Treatment Plan Core Endpoint

#### 1. **Create Treatment Plan**
- **Method:** `POST`
- **URL:** `/api/patients/{patientId}/treatment-plans`
- **Description:** Creates a comprehensive treatment plan linking patient, doctor, disease, and medicines
- **Headers:**
  ```
  Content-Type: application/json
  ```
- **URL Parameters:**
  - `patientId` (Integer): The ID of the patient for whom the treatment plan is created

- **Request Body:**
  ```json
  {
      "doctorId": 456,
      "diseaseId": 789,
      "status": "ongoing",
      "notes": "Patient diagnosed with Type 2 diabetes. Monitor blood glucose levels daily. Follow strict diabetic diet and exercise routine.",
      "medicines": [
          {
              "medicineId": 1,
              "dosage": "500mg",
              "timing": "Twice daily with meals"
          },
          {
              "medicineId": 2,
              "dosage": "10 units", 
              "timing": "Before breakfast"
          }
      ]
  }
  ```

- **Success Response (200 OK):**
  ```json
  {
      "treatmentId": 1,
      "patientId": 1,
      "doctorId": 456,
      "diseaseId": 789,
      "status": "ongoing",
      "startDate": "2025-08-01",
      "notes": "Patient diagnosed with Type 2 diabetes...",
      "medicines": [
          {
              "treatmentMedID": 1,
              "medicineName": "Metformin",
              "dosage": "500mg",
              "timing": "Twice daily with meals"
          },
          {
              "treatmentMedID": 2,
              "medicineName": "Insulin",
              "dosage": "10 units",
              "timing": "Before breakfast"
          }
      ]
  }
  ```

- **Error Responses:**
  - `400 Bad Request`: "Patient not found"
  - `500 Internal Server Error`: "Failed to create treatment plan: [error details]"

---

## 👤 PATIENT APIs

#### 2. **Add Patient**
- **Method:** `POST`
- **URL:** `/v1/healsync/patient/add`
- **Headers:**
  ```
  Content-Type: application/json
  ```
- **Request Body:**
  ```json
  {
      "patientName": "John Doe",
      "patientAge": 45,
      "gender": "Male",
      "mobileNo": "1234567890",
      "email": "john.doe@email.com",
      "password": "password123"
  }
  ```
- **Response:**
  ```json
  {
      "patientId": 1,
      "patientName": "John Doe",
      "patientAge": 45,
      "gender": "Male",
      "mobileNo": "1234567890",
      "email": "john.doe@email.com"
  }
  ```

#### 3. **Patient Login**
- **Method:** `POST`
- **URL:** `/v1/healsync/patient/login`
- **Headers:**
  ```
  Content-Type: application/json
  ```
- **Request Body:**
  ```json
  {
      "email": "john.doe@email.com",
      "password": "password123"
  }
  ```
- **Success Response (200 OK):**
  ```json
  {
      "patientId": 1,
      "patientName": "John Doe",
      "patientAge": 45,
      "gender": "Male",
      "mobileNo": "1234567890",
      "email": "john.doe@email.com"
  }
  ```
- **Error Response (401 Unauthorized):**
  ```
  "Invalid email or password"
  ```

---

## 👨‍⚕️ DOCTOR APIs

#### 4. **Add Doctor**
- **Method:** `POST`
- **URL:** `/v1/healsync/doctor/add`
- **Headers:**
  ```
  Content-Type: application/json
  ```
- **Request Body:**
  ```json
  {
      "doctorName": "Dr. Smith",
      "speciality": "Cardiology",
      "experienceYears": 10,
      "mobileNo": "9876543210",
      "email": "dr.smith@hospital.com",
      "fees": 500.00
  }
  ```

#### 5. **Get Doctor by ID**
- **Method:** `GET`
- **URL:** `/v1/healsync/doctor/{doctorId}`
- **Success Response (200 OK):**
  ```json
  {
      "doctorId": 1,
      "doctorName": "Dr. Smith",
      "speciality": "Cardiology",
      "experienceYears": 10,
      "mobileNo": "9876543210",
      "email": "dr.smith@hospital.com",
      "fees": 500.00
  }
  ```
- **Error Response (404 Not Found):**
  ```
  "Doctor not found"
  ```

#### 6. **Update Doctor**
- **Method:** `PUT`
- **URL:** `/v1/healsync/doctor/{doctorId}`
- **Headers:**
  ```
  Content-Type: application/json
  ```
- **Request Body:** Same as Add Doctor

#### 7. **Delete Doctor**
- **Method:** `DELETE`
- **URL:** `/v1/healsync/doctor/{doctorId}`
- **Success Response (200 OK):**
  ```
  "Doctor deleted successfully"
  ```

#### 8. **Get All Doctor Public Profiles**
- **Method:** `GET`
- **URL:** `/v1/healsync/doctor/public-profiles`
- **Response:**
  ```json
  [
      {
          "doctorId": 1,
          "doctorName": "Dr. Smith",
          "speciality": "Cardiology",
          "experienceYears": 10,
          "fees": 500.00
      }
  ]
  ```

---

## 💊 MEDICINE APIs

#### 9. **Add Medicine**
- **Method:** `POST`
- **URL:** `/v1/healsync/medicine/add`
- **Headers:**
  ```
  Content-Type: application/json
  ```
- **Request Body:**
  ```json
  {
      "name": "Metformin",
      "usage": "Diabetes management - helps control blood sugar levels",
      "sideEffect": "Nausea, diarrhea, metallic taste"
  }
  ```

#### 10. **Get All Medicines**
- **Method:** `GET`
- **URL:** `/v1/healsync/medicine/all`
- **Response:**
  ```json
  [
      {
          "medicineId": 1,
          "name": "Metformin",
          "usage": "Diabetes management",
          "sideEffect": "Nausea, diarrhea"
      }
  ]
  ```

#### 11. **Get Medicine by ID**
- **Method:** `GET`
- **URL:** `/v1/healsync/medicine/{medicineId}`

#### 12. **Update Medicine**
- **Method:** `PUT`
- **URL:** `/v1/healsync/medicine/update/{medicineId}`

#### 13. **Delete Medicine**
- **Method:** `DELETE`
- **URL:** `/v1/healsync/medicine/delete/{medicineId}`

---

## 🦠 DISEASE APIs

#### 14. **Get Disease Details**
- **Method:** `GET`
- **URL:** `/v1/healsync/disease/details`
- **Query Parameters:**
  - `id` (Integer, optional): Disease ID
  - `name` (String, optional): Disease name
- **Example:**
  ```
  GET /v1/healsync/disease/details?id=1
  GET /v1/healsync/disease/details?name=Diabetes
  ```

#### 15. **Get All Diseases**
- **Method:** `GET`
- **URL:** `/v1/healsync/disease/all`

#### 16. **Add Disease**
- **Method:** `POST`
- **URL:** `/v1/healsync/disease/add`

#### 17. **Update Disease**
- **Method:** `PUT`
- **URL:** `/v1/healsync/disease/update/{diseaseId}`

#### 18. **Delete Disease**
- **Method:** `DELETE`
- **URL:** `/v1/healsync/disease/delete/{diseaseId}`

---

## 📅 APPOINTMENT APIs

#### 19. **Book Appointment**
- **Method:** `POST`
- **URL:** `/v1/healsync/book/appointment`
- **Query Parameters:**
  - `speciality` (String): Doctor speciality
  - `startDateTime` (String): ISO format datetime
  - `endDateTime` (String): ISO format datetime  
  - `patientId` (Integer): Patient ID
- **Example:**
  ```
  POST /v1/healsync/book/appointment?speciality=Cardiology&startDateTime=2025-08-02T10:00:00&endDateTime=2025-08-02T11:00:00&patientId=1
  ```

#### 20. **Cancel Appointment**
- **Method:** `POST`
- **URL:** `/v1/healsync/book/cancel`
- **Query Parameters:**
  - `appointmentId` (Integer): Appointment ID
  - `doctorId` (Integer): Doctor ID

---

## 😊 EMOTION TRACKER APIs

#### 21. **Track Emotion**
- **Method:** `POST`
- **URL:** `/v1/healsync/emotion/track`
- **Headers:**
  ```
  Content-Type: application/json
  ```
- **Request Body:**
  ```json
  {
      "patientId": 1,
      "emotion": "happy",
      "intensity": 8,
      "notes": "Feeling good today after treatment"
  }
  ```

#### 22. **Get Patient Emotions**
- **Method:** `GET`
- **URL:** `/v1/healsync/emotion/patient/{patientId}`
- **Response:**
  ```json
  [
      {
          "emotionId": 1,
          "patientId": 1,
          "emotion": "happy",
          "intensity": 8,
          "timestamp": "2025-08-01T10:30:00",
          "notes": "Feeling good today"
      }
  ]
  ```

---

## 🔗 TREATMENT PLAN WORKFLOW

### Complete Treatment Plan Creation Flow:

1. **Create Patient** (API #2)
2. **Create Medicines** (API #9) - Create all medicines that will be prescribed
3. **Create Treatment Plan** (API #1) - Links patient, doctor, disease, and medicines

### Example Complete Workflow:

#### Step 1: Create Patient
```bash
POST /v1/healsync/patient/add
Body: {"patientName": "John Doe", "patientAge": 45, ...}
Response: {"patientId": 1, ...}
```

#### Step 2: Create Medicines
```bash
POST /v1/healsync/medicine/add
Body: {"name": "Metformin", "usage": "Diabetes management", ...}
Response: {"medicineId": 1, ...}

POST /v1/healsync/medicine/add  
Body: {"name": "Insulin", "usage": "Blood glucose control", ...}
Response: {"medicineId": 2, ...}
```

#### Step 3: Create Treatment Plan
```bash
POST /api/patients/1/treatment-plans
Body: {
    "doctorId": 456,
    "diseaseId": 789,
    "medicines": [
        {"medicineId": 1, "dosage": "500mg", "timing": "Twice daily"},
        {"medicineId": 2, "dosage": "10 units", "timing": "Before breakfast"}
    ]
}
```

---

## 📋 REQUEST/RESPONSE FORMATS

### Treatment Plan Request Format:
```json
{
    "doctorId": Integer,
    "diseaseId": Integer, 
    "status": String,
    "notes": String,
    "medicines": [
        {
            "medicineId": Integer,
            "dosage": String,
            "timing": String
        }
    ]
}
```

### Treatment Plan Response Format:
```json
{
    "treatmentId": Integer,
    "patientId": Integer,
    "doctorId": Integer,
    "diseaseId": Integer,
    "status": String,
    "startDate": "YYYY-MM-DD",
    "notes": String,
    "medicines": [
        {
            "treatmentMedID": Integer,
            "medicineName": String,
            "dosage": String,
            "timing": String
        }
    ]
}
```

---

## 🎯 KEY FEATURES

### Treatment Plan System:
- ✅ **Patient Validation**: Checks if patient exists before creating plan
- ✅ **Medicine Resolution**: Automatically resolves medicine names from IDs  
- ✅ **Automatic Dating**: Sets start date to current date
- ✅ **Transactional Safety**: All operations are wrapped in database transactions
- ✅ **Error Handling**: Comprehensive error responses for different scenarios
- ✅ **Flexible Medicine List**: Can add multiple medicines with dosage and timing

### System Architecture:
- ✅ **Spring Boot Backend** with REST APIs
- ✅ **JPA/Hibernate** for database operations
- ✅ **DTO Pattern** for clean data transfer
- ✅ **Service Layer** for business logic
- ✅ **Repository Pattern** for data access

---

## 🔧 TESTING RECOMMENDATIONS

### Priority Testing Order:
1. **Health Check** - Verify backend is running
2. **Create Patient** - Set up test data
3. **Create Medicines** - Set up medicine catalog  
4. **Create Treatment Plan** - Test main functionality
5. **Error Scenarios** - Test invalid patient IDs, etc.

### Sample Test Data:

**Patient:**
```json
{
    "patientName": "John Doe",
    "patientAge": 45,
    "gender": "Male", 
    "mobileNo": "1234567890",
    "email": "john.doe@email.com",
    "password": "password123"
}
```

**Medicines:**
```json
[
    {"name": "Metformin", "usage": "Diabetes management", "sideEffect": "Nausea, diarrhea"},
    {"name": "Insulin", "usage": "Blood glucose control", "sideEffect": "Hypoglycemia"},
    {"name": "Lisinopril", "usage": "Blood pressure control", "sideEffect": "Dry cough"}
]
```

**Treatment Plan:**
```json
{
    "doctorId": 456,
    "diseaseId": 789,
    "status": "ongoing",
    "notes": "Monitor blood glucose daily. Follow diabetic diet.",
    "medicines": [
        {"medicineId": 1, "dosage": "500mg", "timing": "Twice daily with meals"},
        {"medicineId": 2, "dosage": "10 units", "timing": "Before breakfast"}
    ]
}
```

---

## 🚨 IMPORTANT NOTES

1. **Patient ID is required** in the URL path for treatment plan creation
2. **Medicine IDs must exist** in the database before creating treatment plans
3. **startDate is auto-generated** to current date
4. **Medicine names are auto-resolved** from medicineId in the request
5. **All endpoints use JSON** for request/response bodies
6. **Error responses** include descriptive messages for debugging

This covers the complete Treatment Plan system and all related APIs in your HealSync backend! 🎉
