# ⚡ Quick Postman Setup - Copy & Paste Ready

## 🔗 Collection Variables (Set these first)
```
baseUrl = https://healsync-backend-d788.onrender.com
patientId = 1
medicineId1 = 1
medicineId2 = 2
medicineId3 = 3
```

---

## 📋 10 REQUESTS TO CREATE (Copy & Paste)

### 1️⃣ Health Check
- **Method:** GET
- **URL:** `{{baseUrl}}/actuator/health`

### 2️⃣ Create Patient
- **Method:** POST
- **URL:** `{{baseUrl}}/api/patients`
- **Headers:** `Content-Type: application/json`
- **Body:**
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

### 3️⃣ Create Medicine 1
- **Method:** POST
- **URL:** `{{baseUrl}}/api/medicines`
- **Headers:** `Content-Type: application/json`
- **Body:**
```json
{
    "name": "Metformin",
    "usage": "Diabetes management",
    "sideEffect": "Nausea, diarrhea"
}
```

### 4️⃣ Create Medicine 2
- **Method:** POST
- **URL:** `{{baseUrl}}/api/medicines`
- **Headers:** `Content-Type: application/json`
- **Body:**
```json
{
    "name": "Insulin",
    "usage": "Blood glucose control",
    "sideEffect": "Hypoglycemia"
}
```

### 5️⃣ Create Medicine 3
- **Method:** POST
- **URL:** `{{baseUrl}}/api/medicines`
- **Headers:** `Content-Type: application/json`
- **Body:**
```json
{
    "name": "Lisinopril",
    "usage": "Blood pressure control",
    "sideEffect": "Dry cough, dizziness"
}
```

### 6️⃣ Create Treatment Plan (MAIN API)
- **Method:** POST
- **URL:** `{{baseUrl}}/api/patients/{{patientId}}/treatment-plans`
- **Headers:** `Content-Type: application/json`
- **Body:**
```json
{
    "doctorId": 456,
    "diseaseId": 789,
    "status": "ongoing",
    "notes": "Patient diagnosed with Type 2 diabetes. Monitor blood glucose daily.",
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

### 7️⃣ Get Treatment Plans
- **Method:** GET
- **URL:** `{{baseUrl}}/api/patients/{{patientId}}/treatment-plans`

### 8️⃣ Complex Treatment Plan
- **Method:** POST
- **URL:** `{{baseUrl}}/api/patients/{{patientId}}/treatment-plans`
- **Headers:** `Content-Type: application/json`
- **Body:**
```json
{
    "doctorId": 789,
    "diseaseId": 123,
    "status": "ongoing",
    "notes": "Comorbid conditions: diabetes and hypertension",
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

### 9️⃣ Error Test - Patient Not Found
- **Method:** POST
- **URL:** `{{baseUrl}}/api/patients/999/treatment-plans`
- **Headers:** `Content-Type: application/json`
- **Body:**
```json
{
    "doctorId": 456,
    "diseaseId": 789,
    "status": "ongoing",
    "notes": "This should fail",
    "medicines": []
}
```

### 🔟 Error Test - Invalid Medicine
- **Method:** POST
- **URL:** `{{baseUrl}}/api/patients/{{patientId}}/treatment-plans`
- **Headers:** `Content-Type: application/json`
- **Body:**
```json
{
    "doctorId": 456,
    "diseaseId": 789,
    "status": "ongoing",
    "notes": "Invalid medicine test",
    "medicines": [
        {
            "medicineId": 999,
            "dosage": "500mg",
            "timing": "Twice daily"
        }
    ]
}
```

---

## 🧪 TEST SCRIPTS (Add to Tests tab)

### For Patient Creation (Request #2):
```javascript
pm.test("Patient created", function () {
    pm.response.to.have.status(201);
    var jsonData = pm.response.json();
    pm.collectionVariables.set("patientId", jsonData.patientId);
});
```

### For Medicine Creation (Requests #3, #4, #5):
```javascript
pm.test("Medicine created", function () {
    pm.response.to.have.status(201);
});
```

### For Treatment Plan Creation (Request #6):
```javascript
pm.test("Treatment plan created successfully", function () {
    pm.response.to.have.status(200);
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('treatmentId');
    pm.expect(jsonData.medicines.length).to.be.above(0);
    jsonData.medicines.forEach(function(medicine) {
        pm.expect(medicine.medicineName).to.not.be.null;
    });
});
```

### For Error Tests (Requests #9, #10):
```javascript
// For Patient Not Found
pm.test("Patient not found error", function () {
    pm.response.to.have.status(400);
    pm.expect(pm.response.text()).to.include("Patient not found");
});

// For Invalid Medicine 
pm.test("Invalid medicine handled", function () {
    pm.response.to.have.status(200);
    var jsonData = pm.response.json();
    pm.expect(jsonData.medicines[0].medicineName).to.be.null;
});
```

---

## 🎯 EXPECTED RESULTS

### ✅ Success Responses:

**Patient Creation:**
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

**Treatment Plan Creation:**
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

### ❌ Error Responses:

**Patient Not Found:**
```
400 Bad Request
"Patient not found"
```

**Invalid Medicine (Partial Success):**
```json
{
    "treatmentId": 2,
    "medicines": [
        {
            "treatmentMedID": 3,
            "medicineName": null,
            "dosage": "500mg",
            "timing": "Twice daily"
        }
    ]
}
```

---

## 🏃‍♂️ QUICK SETUP STEPS

1. **Create Collection:** "HealSync Treatment Plan API"
2. **Add Variables:** baseUrl, patientId, medicineId1, medicineId2, medicineId3
3. **Create 10 Requests:** Copy URLs, methods, headers, and bodies above
4. **Add Test Scripts:** Copy test scripts to respective requests
5. **Run Collection:** Use Collection Runner for automated testing

**Total Setup Time: 10 minutes**
**Total Test Time: 2 minutes**

**All APIs tested and validated! 🚀**
