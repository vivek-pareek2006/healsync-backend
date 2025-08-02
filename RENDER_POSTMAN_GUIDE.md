# 🌐 Complete Postman Guide for HealSync Backend on Render

## 🔗 Backend URL: https://healsync-backend-d788.onrender.com

---

## 🚀 STEP 1: Test Backend Connection

### Test Server Health
```
GET https://healsync-backend-d788.onrender.com/actuator/health
```

**Expected Response:**
```json
{
    "status": "UP"
}
```

If this fails, the backend might be sleeping (Render free tier). Wait 30-60 seconds and try again.

---

## 📋 STEP 2: Create Required Test Data

### 2.1 Create a Patient (REQUIRED FIRST)

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

**Expected Response (201 Created):**
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

⚠️ **IMPORTANT: Save the `patientId` from this response!**

### 2.2 Create Medicines (REQUIRED)

**Medicine 1 - Metformin:**
```
POST https://healsync-backend-d788.onrender.com/api/medicines
Content-Type: application/json

{
    "name": "Metformin",
    "usage": "Diabetes management - helps control blood sugar levels",
    "sideEffect": "Nausea, diarrhea, metallic taste in mouth"
}
```

**Medicine 2 - Insulin:**
```
POST https://healsync-backend-d788.onrender.com/api/medicines
Content-Type: application/json

{
    "name": "Insulin",
    "usage": "Blood glucose control for diabetes",
    "sideEffect": "Hypoglycemia, injection site reactions"
}
```

**Medicine 3 - Lisinopril:**
```
POST https://healsync-backend-d788.onrender.com/api/medicines
Content-Type: application/json

{
    "name": "Lisinopril",
    "usage": "Blood pressure control",
    "sideEffect": "Dry cough, dizziness, fatigue"
}
```

**Expected Response for each (201 Created):**
```json
{
    "medicineId": 1,
    "name": "Metformin",
    "usage": "Diabetes management - helps control blood sugar levels",
    "sideEffect": "Nausea, diarrhea, metallic taste in mouth"
}
```

⚠️ **IMPORTANT: Note the `medicineId` values (usually 1, 2, 3)!**

---

## 🎯 STEP 3: Treatment Plan API Tests

### 3.1 ✅ Create Treatment Plan (Main API)

```
POST https://healsync-backend-d788.onrender.com/api/patients/1/treatment-plans
Content-Type: application/json

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

**Expected Response (200 OK):**
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

### 3.2 ✅ Get Patient's Treatment Plans

```
GET https://healsync-backend-d788.onrender.com/api/patients/1/treatment-plans
```

**Expected Response (200 OK):**
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
    }
]
```

---

## 🧪 STEP 4: Advanced Treatment Plan Tests

### 4.1 Create Complex Treatment Plan (Multiple Medicines)

```
POST https://healsync-backend-d788.onrender.com/api/patients/1/treatment-plans
Content-Type: application/json

{
    "doctorId": 789,
    "diseaseId": 123,
    "status": "ongoing",
    "notes": "Patient has both diabetes and hypertension. Requires comprehensive treatment plan with multiple medications and lifestyle changes.",
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

### 4.2 Create Treatment Plan with No Medicines

```
POST https://healsync-backend-d788.onrender.com/api/patients/1/treatment-plans
Content-Type: application/json

{
    "doctorId": 321,
    "diseaseId": 456,
    "status": "ongoing",
    "notes": "Lifestyle modification treatment plan. No medications required at this time. Focus on diet and exercise.",
    "medicines": []
}
```

---

## ❌ STEP 5: Error Testing

### 5.1 Test Patient Not Found

```
POST https://healsync-backend-d788.onrender.com/api/patients/999/treatment-plans
Content-Type: application/json

{
    "doctorId": 456,
    "diseaseId": 789,
    "status": "ongoing",
    "notes": "This should fail - patient doesn't exist",
    "medicines": []
}
```

**Expected Response (400 Bad Request):**
```
Patient not found
```

### 5.2 Test Invalid Medicine ID

```
POST https://healsync-backend-d788.onrender.com/api/patients/1/treatment-plans
Content-Type: application/json

{
    "doctorId": 456,
    "diseaseId": 789,
    "status": "ongoing",
    "notes": "Testing with invalid medicine ID",
    "medicines": [
        {
            "medicineId": 999,
            "dosage": "500mg",
            "timing": "Twice daily"
        }
    ]
}
```

**Expected Behavior:**
- Treatment plan created successfully
- Medicine name will be `null` in response

### 5.3 Test Missing Required Fields

```
POST https://healsync-backend-d788.onrender.com/api/patients/1/treatment-plans
Content-Type: application/json

{
    "status": "ongoing",
    "medicines": []
}
```

**Expected Response (400 Bad Request):**
Validation error for missing doctorId and diseaseId

---

## 📊 STEP 6: Additional API Endpoints

### 6.1 Get All Patients

```
GET https://healsync-backend-d788.onrender.com/api/patients
```

### 6.2 Get All Medicines

```
GET https://healsync-backend-d788.onrender.com/api/medicines
```

### 6.3 Get Specific Patient

```
GET https://healsync-backend-d788.onrender.com/api/patients/1
```

### 6.4 Get Specific Medicine

```
GET https://healsync-backend-d788.onrender.com/api/medicines/1
```

---

## 🔧 STEP 7: Postman Collection Setup

### Collection Variables
1. Create new collection: "HealSync Treatment Plan API"
2. Set variables:

| Variable | Value |
|----------|--------|
| baseUrl | https://healsync-backend-d788.onrender.com |
| patientId | 1 |
| medicineId1 | 1 |
| medicineId2 | 2 |
| medicineId3 | 3 |

### Test Scripts (Add to Tests tab)

**For successful requests:**
```javascript
pm.test("Status code is 200 or 201", function () {
    pm.expect(pm.response.code).to.be.oneOf([200, 201]);
});

pm.test("Response time is acceptable", function () {
    pm.expect(pm.response.responseTime).to.be.below(5000);
});

pm.test("Response has required fields", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.be.an('object');
});
```

**For treatment plan creation:**
```javascript
pm.test("Treatment plan created successfully", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('treatmentId');
    pm.expect(jsonData).to.have.property('medicines');
    pm.expect(jsonData.medicines).to.be.an('array');
});

pm.test("Medicine names are resolved", function () {
    var jsonData = pm.response.json();
    jsonData.medicines.forEach(function(medicine) {
        pm.expect(medicine).to.have.property('medicineName');
        pm.expect(medicine).to.have.property('dosage');
        pm.expect(medicine).to.have.property('timing');
    });
});
```

---

## 🎯 STEP 8: Complete Test Sequence

### Run tests in this order:

1. **Health Check** → Verify backend is running
2. **Create Patient** → Get patientId
3. **Create Medicines** → Get medicineIds
4. **Create Treatment Plan** → Main functionality
5. **Get Treatment Plans** → Verify data retrieval
6. **Error Tests** → Validate error handling
7. **Complex Scenarios** → Multiple medicines, edge cases

---

## 🐛 Common Issues & Solutions

### ❌ "Failed to fetch" or "Network Error"
**Solution:** Backend might be sleeping on Render. Wait 30-60 seconds and try again.

### ❌ "Patient not found"
**Solution:** Make sure you created a patient first and use the correct patientId.

### ❌ "500 Internal Server Error"
**Solution:** Check if all required fields are provided in the request body.

### ❌ "404 Not Found"
**Solution:** Verify the URL is correct. Check for typos in the endpoint path.

---

## ✅ Success Checklist

- [ ] Backend health check passes
- [ ] Patient created successfully
- [ ] Medicines created successfully
- [ ] Treatment plan created with medicines
- [ ] Medicine names resolved correctly
- [ ] Error handling works for invalid patient
- [ ] GET endpoint returns treatment plans
- [ ] Complex scenarios work (multiple medicines)

---

## 🎉 Expected Final Result

After successful testing, you should have:

1. **1 Patient** in the system (John Doe)
2. **3 Medicines** in the system (Metformin, Insulin, Lisinopril)
3. **2+ Treatment Plans** for the patient
4. **Multiple Treatment Medicines** linked to treatment plans
5. **Complete API validation** working correctly

**Your HealSync Treatment Plan API is fully functional! 🚀**
