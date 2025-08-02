# 🚀 Quick Postman Reference - HealSync on Render

## 🌐 Base URL: https://healsync-backend-d788.onrender.com

---

## 🏁 QUICK START (Copy & Paste Ready)

### 1️⃣ TEST BACKEND (Run this first)
```
GET https://healsync-backend-d788.onrender.com/actuator/health
```
**Wait if it fails - Render backend might be sleeping!**

### 2️⃣ CREATE PATIENT (Save the patientId!)
```
POST https://healsync-backend-d788.onrender.com/api/patients
Content-Type: application/json

{
    "patientName": "John Doe",
    "patientAge": 45,
    "gender": "Male",
    "mobileNo": "1234567890",
    "email": "john.doe@email.com",
    "password": "password123"
}
```

### 3️⃣ CREATE MEDICINES (Run all 3)
**Medicine 1:**
```
POST https://healsync-backend-d788.onrender.com/api/medicines
Content-Type: application/json

{
    "name": "Metformin",
    "usage": "Diabetes management",
    "sideEffect": "Nausea, diarrhea"
}
```

**Medicine 2:**
```
POST https://healsync-backend-d788.onrender.com/api/medicines
Content-Type: application/json

{
    "name": "Insulin",
    "usage": "Blood glucose control",
    "sideEffect": "Hypoglycemia"
}
```

**Medicine 3:**
```
POST https://healsync-backend-d788.onrender.com/api/medicines
Content-Type: application/json

{
    "name": "Lisinopril",
    "usage": "Blood pressure control",
    "sideEffect": "Dry cough, dizziness"
}
```

---

## 🎯 MAIN TREATMENT PLAN API

### ✅ CREATE TREATMENT PLAN
```
POST https://healsync-backend-d788.onrender.com/api/patients/1/treatment-plans
Content-Type: application/json

{
    "doctorId": 456,
    "diseaseId": 789,
    "status": "ongoing",
    "notes": "Patient has Type 2 diabetes. Monitor blood glucose daily. Follow diabetic diet.",
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

**Expected Success Response:**
```json
{
    "treatmentId": 1,
    "patientId": 1,
    "doctorId": 456,
    "diseaseId": 789,
    "status": "ongoing",
    "startDate": "2025-08-01",
    "notes": "Patient has Type 2 diabetes...",
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

### ✅ GET TREATMENT PLANS
```
GET https://healsync-backend-d788.onrender.com/api/patients/1/treatment-plans
```

---

## 🧪 ERROR TESTS

### ❌ Patient Not Found
```
POST https://healsync-backend-d788.onrender.com/api/patients/999/treatment-plans
Content-Type: application/json

{
    "doctorId": 456,
    "diseaseId": 789,
    "status": "ongoing",
    "notes": "This should fail",
    "medicines": []
}
```
**Expected:** `400 Bad Request` with "Patient not found"

### ⚠️ Invalid Medicine ID
```
POST https://healsync-backend-d788.onrender.com/api/patients/1/treatment-plans
Content-Type: application/json

{
    "doctorId": 456,
    "diseaseId": 789,
    "status": "ongoing",
    "notes": "Testing invalid medicine",
    "medicines": [
        {
            "medicineId": 999,
            "dosage": "500mg",
            "timing": "Twice daily"
        }
    ]
}
```
**Expected:** Treatment created but `medicineName` = `null`

---

## 📊 ADDITIONAL USEFUL ENDPOINTS

### View All Patients
```
GET https://healsync-backend-d788.onrender.com/api/patients
```

### View All Medicines
```
GET https://healsync-backend-d788.onrender.com/api/medicines
```

### View Specific Patient
```
GET https://healsync-backend-d788.onrender.com/api/patients/1
```

### View Specific Medicine
```
GET https://healsync-backend-d788.onrender.com/api/medicines/1
```

---

## 🎯 COMPLEX TEST SCENARIOS

### Multiple Medicines Treatment
```
POST https://healsync-backend-d788.onrender.com/api/patients/1/treatment-plans
Content-Type: application/json

{
    "doctorId": 789,
    "diseaseId": 123,
    "status": "ongoing",
    "notes": "Complex treatment with multiple conditions",
    "medicines": [
        {
            "medicineId": 1,
            "dosage": "500mg",
            "timing": "With breakfast and dinner"
        },
        {
            "medicineId": 2,
            "dosage": "15 units",
            "timing": "Before breakfast"
        },
        {
            "medicineId": 3,
            "dosage": "10mg",
            "timing": "Once daily morning"
        }
    ]
}
```

### No Medicines Treatment (Lifestyle Only)
```
POST https://healsync-backend-d788.onrender.com/api/patients/1/treatment-plans
Content-Type: application/json

{
    "doctorId": 321,
    "diseaseId": 456,
    "status": "ongoing",
    "notes": "Lifestyle modification only - diet and exercise",
    "medicines": []
}
```

---

## 🔧 POSTMAN SETUP TIPS

### Collection Variables:
- `baseUrl`: `https://healsync-backend-d788.onrender.com`
- `patientId`: `1` (update after patient creation)

### Headers for POST requests:
```
Content-Type: application/json
```

### Quick Test Script:
```javascript
pm.test("Status code is success", function () {
    pm.expect(pm.response.code).to.be.oneOf([200, 201]);
});

pm.test("Response time under 5s", function () {
    pm.expect(pm.response.responseTime).to.be.below(5000);
});
```

---

## 🚨 IMPORTANT NOTES

⚠️ **Render Free Tier:** Backend may sleep after 15 minutes of inactivity. First request might take 30-60 seconds.

✅ **Test Order:** Always run Health Check → Create Patient → Create Medicines → Create Treatment Plan

⚠️ **Patient ID:** Replace `1` with actual patientId from patient creation response

⚠️ **Medicine IDs:** Use actual medicineIds from medicine creation responses

---

## 🎯 SUCCESS VALIDATION

✅ Health check returns `{"status":"UP"}`
✅ Patient created with patientId
✅ 3 medicines created with medicineIds
✅ Treatment plan created with resolved medicine names
✅ GET request returns treatment plans array
✅ Error handling works for invalid patient ID

**You're all set! 🎉**
