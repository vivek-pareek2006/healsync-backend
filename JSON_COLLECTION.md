# 📄 Complete JSON Collection for HealSync Treatment Plan API

## 🌐 Base URL: https://healsync-backend-d788.onrender.com

---

## 🏁 STEP 1: Health Check (No JSON needed)

```
GET https://healsync-backend-d788.onrender.com/actuator/health
```

---

## 👤 STEP 2: Create Patient

### Request JSON:
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

### Expected Response JSON:
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

---

## 💊 STEP 3: Create Medicines

### Medicine 1 - Metformin
**Request JSON:**
```json
{
    "name": "Metformin",
    "usage": "Diabetes management - helps control blood sugar levels by reducing glucose production in liver",
    "sideEffect": "Nausea, diarrhea, metallic taste in mouth, stomach upset"
}
```

**Expected Response JSON:**
```json
{
    "medicineId": 1,
    "name": "Metformin",
    "usage": "Diabetes management - helps control blood sugar levels by reducing glucose production in liver",
    "sideEffect": "Nausea, diarrhea, metallic taste in mouth, stomach upset"
}
```

### Medicine 2 - Insulin
**Request JSON:**
```json
{
    "name": "Insulin",
    "usage": "Blood glucose control for diabetes - helps cells absorb glucose from bloodstream",
    "sideEffect": "Hypoglycemia, injection site reactions, weight gain"
}
```

**Expected Response JSON:**
```json
{
    "medicineId": 2,
    "name": "Insulin",
    "usage": "Blood glucose control for diabetes - helps cells absorb glucose from bloodstream",
    "sideEffect": "Hypoglycemia, injection site reactions, weight gain"
}
```

### Medicine 3 - Lisinopril
**Request JSON:**
```json
{
    "name": "Lisinopril",
    "usage": "Blood pressure control - ACE inhibitor for hypertension management",
    "sideEffect": "Dry cough, dizziness, fatigue, elevated potassium levels"
}
```

**Expected Response JSON:**
```json
{
    "medicineId": 3,
    "name": "Lisinopril",
    "usage": "Blood pressure control - ACE inhibitor for hypertension management",
    "sideEffect": "Dry cough, dizziness, fatigue, elevated potassium levels"
}
```

---

## 🏥 STEP 4: Treatment Plan APIs

### 4.1 ✅ Create Basic Treatment Plan

**Request JSON:**
```json
{
    "doctorId": 456,
    "diseaseId": 789,
    "status": "ongoing",
    "notes": "Patient diagnosed with Type 2 diabetes. Monitor blood glucose levels daily. Follow strict diabetic diet and exercise routine. Regular check-ups every 3 months.",
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

**Expected Response JSON:**
```json
{
    "treatmentId": 1,
    "patientId": 1,
    "doctorId": 456,
    "diseaseId": 789,
    "status": "ongoing",
    "startDate": "2025-08-01",
    "notes": "Patient diagnosed with Type 2 diabetes. Monitor blood glucose levels daily. Follow strict diabetic diet and exercise routine. Regular check-ups every 3 months.",
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

### 4.2 ✅ Create Complex Treatment Plan (3 Medicines)

**Request JSON:**
```json
{
    "doctorId": 789,
    "diseaseId": 123,
    "status": "ongoing",
    "notes": "Patient has comorbid conditions: Type 2 diabetes and hypertension. Requires comprehensive treatment plan with lifestyle modifications. Monitor blood pressure and glucose levels daily.",
    "medicines": [
        {
            "medicineId": 1,
            "dosage": "500mg",
            "timing": "Twice daily with breakfast and dinner"
        },
        {
            "medicineId": 2,
            "dosage": "15 units",
            "timing": "Before breakfast"
        },
        {
            "medicineId": 3,
            "dosage": "10mg",
            "timing": "Once daily in the morning"
        }
    ]
}
```

**Expected Response JSON:**
```json
{
    "treatmentId": 2,
    "patientId": 1,
    "doctorId": 789,
    "diseaseId": 123,
    "status": "ongoing",
    "startDate": "2025-08-01",
    "notes": "Patient has comorbid conditions: Type 2 diabetes and hypertension. Requires comprehensive treatment plan with lifestyle modifications. Monitor blood pressure and glucose levels daily.",
    "medicines": [
        {
            "treatmentMedID": 3,
            "medicineName": "Metformin",
            "dosage": "500mg",
            "timing": "Twice daily with breakfast and dinner"
        },
        {
            "treatmentMedID": 4,
            "medicineName": "Insulin",
            "dosage": "15 units",
            "timing": "Before breakfast"
        },
        {
            "treatmentMedID": 5,
            "medicineName": "Lisinopril",
            "dosage": "10mg",
            "timing": "Once daily in the morning"
        }
    ]
}
```

### 4.3 ✅ Create Treatment Plan with No Medicines

**Request JSON:**
```json
{
    "doctorId": 321,
    "diseaseId": 456,
    "status": "ongoing",
    "notes": "Pre-diabetes management through lifestyle modification only. No medications required at this time. Focus on diet control, regular exercise, and weight management. Follow-up in 6 weeks.",
    "medicines": []
}
```

**Expected Response JSON:**
```json
{
    "treatmentId": 3,
    "patientId": 1,
    "doctorId": 321,
    "diseaseId": 456,
    "status": "ongoing",
    "startDate": "2025-08-01",
    "notes": "Pre-diabetes management through lifestyle modification only. No medications required at this time. Focus on diet control, regular exercise, and weight management. Follow-up in 6 weeks.",
    "medicines": []
}
```

### 4.4 ✅ Create Treatment Plan with Different Status

**Request JSON:**
```json
{
    "doctorId": 654,
    "diseaseId": 321,
    "status": "completed",
    "notes": "Short-term antibiotic treatment for infection. Treatment completed successfully. Patient recovered fully.",
    "medicines": [
        {
            "medicineId": 1,
            "dosage": "250mg",
            "timing": "Three times daily for 7 days"
        }
    ]
}
```

**Expected Response JSON:**
```json
{
    "treatmentId": 4,
    "patientId": 1,
    "doctorId": 654,
    "diseaseId": 321,
    "status": "completed",
    "startDate": "2025-08-01",
    "notes": "Short-term antibiotic treatment for infection. Treatment completed successfully. Patient recovered fully.",
    "medicines": [
        {
            "treatmentMedID": 6,
            "medicineName": "Metformin",
            "dosage": "250mg",
            "timing": "Three times daily for 7 days"
        }
    ]
}
```

---

## ❌ STEP 5: Error Testing JSONs

### 5.1 Patient Not Found Error

**Request JSON:**
```json
{
    "doctorId": 456,
    "diseaseId": 789,
    "status": "ongoing",
    "notes": "This should fail because patient ID 999 doesn't exist",
    "medicines": [
        {
            "medicineId": 1,
            "dosage": "500mg",
            "timing": "Twice daily"
        }
    ]
}
```

**Expected Error Response:**
```
400 Bad Request
"Patient not found"
```

### 5.2 Invalid Medicine ID Test

**Request JSON:**
```json
{
    "doctorId": 456,
    "diseaseId": 789,
    "status": "ongoing",
    "notes": "Testing with invalid medicine ID 999 - should create treatment but medicine name will be null",
    "medicines": [
        {
            "medicineId": 999,
            "dosage": "500mg",
            "timing": "Twice daily"
        }
    ]
}
```

**Expected Response JSON:**
```json
{
    "treatmentId": 5,
    "patientId": 1,
    "doctorId": 456,
    "diseaseId": 789,
    "status": "ongoing",
    "startDate": "2025-08-01",
    "notes": "Testing with invalid medicine ID 999 - should create treatment but medicine name will be null",
    "medicines": [
        {
            "treatmentMedID": 7,
            "medicineName": null,
            "dosage": "500mg",
            "timing": "Twice daily"
        }
    ]
}
```

### 5.3 Missing Required Fields Error

**Request JSON:**
```json
{
    "status": "ongoing",
    "notes": "Missing doctorId and diseaseId - should fail validation",
    "medicines": []
}
```

**Expected Error Response:**
```
400 Bad Request
"Validation error" or constraint violation message
```

### 5.4 Empty Request Body Error

**Request JSON:**
```json
{}
```

**Expected Error Response:**
```
400 Bad Request
"Validation error" or "Required fields missing"
```

---

## 📊 STEP 6: Get Treatment Plans (No Request JSON needed)

### Get All Treatment Plans for Patient
```
GET https://healsync-backend-d788.onrender.com/api/patients/1/treatment-plans
```

**Expected Response JSON:**
```json
[
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
    },
    {
        "treatmentId": 2,
        "patientId": 1,
        "doctorId": 789,
        "diseaseId": 123,
        "status": "ongoing",
        "startDate": "2025-08-01",
        "notes": "Patient has comorbid conditions...",
        "medicines": [...]
    }
]
```

---

## 🔧 Additional API JSONs

### Create Another Patient for Testing

**Request JSON:**
```json
{
    "patientName": "Jane Smith",
    "patientAge": 32,
    "gender": "Female",
    "mobileNo": "0987654321",
    "email": "jane.smith@email.com",
    "password": "password456"
}
```

### Create Additional Medicine

**Request JSON:**
```json
{
    "name": "Aspirin",
    "usage": "Pain relief and anti-inflammatory - also used for heart disease prevention",
    "sideEffect": "Stomach irritation, bleeding risk, allergic reactions"
}
```

---

## 🎯 Status Options for Treatment Plans

You can use these different status values in your JSON:

```json
{
    "status": "ongoing"     // Active treatment
}
```

```json
{
    "status": "completed"   // Treatment finished
}
```

```json
{
    "status": "paused"      // Temporarily stopped
}
```

```json
{
    "status": "cancelled"   // Treatment discontinued
}
```

---

## 📋 Quick Copy-Paste Headers

For all POST requests, add this header in Postman:
```
Content-Type: application/json
```

---

## 🎉 Testing Summary

After running all these JSON tests, you'll have validated:

✅ **Basic treatment plan creation**
✅ **Complex multi-medicine treatment plans**
✅ **Lifestyle-only treatment plans (no medicines)**
✅ **Different treatment statuses**
✅ **Error handling for invalid patients**
✅ **Error handling for invalid medicines**
✅ **Medicine name resolution from database**
✅ **Complete API response structure**

**All JSON payloads are ready for copy-paste into Postman! 🚀**
