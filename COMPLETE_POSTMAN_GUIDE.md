# 🚀 Complete Postman Setup Guide - HealSync Treatment Plan API

## 🌐 Backend URL: https://healsync-backend-d788.onrender.com

---

## 📋 COMPLETE API ENDPOINTS LIST

### 🏥 Treatment Plan APIs
1. `POST /api/patients/{patientId}/treatment-plans` - Create treatment plan
2. `GET /api/patients/{patientId}/treatment-plans` - Get patient's treatment plans

### 👤 Patient APIs  
3. `POST /api/patients` - Create patient
4. `GET /api/patients` - Get all patients
5. `GET /api/patients/{id}` - Get specific patient

### 💊 Medicine APIs
6. `POST /api/medicines` - Create medicine
7. `GET /api/medicines` - Get all medicines  
8. `GET /api/medicines/{id}` - Get specific medicine

### 🔧 System APIs
9. `GET /actuator/health` - Check backend status

---

## 🎯 POSTMAN COLLECTION SETUP

### Step 1: Create New Collection
1. Open Postman
2. Click "New" → "Collection"
3. Name: "HealSync Treatment Plan API"
4. Description: "Complete API testing for treatment plans"

### Step 2: Set Collection Variables
1. Go to Collection Settings → Variables tab
2. Add these variables:

| Variable | Initial Value | Current Value |
|----------|---------------|---------------|
| baseUrl | https://healsync-backend-d788.onrender.com | https://healsync-backend-d788.onrender.com |
| patientId | 1 | 1 |
| medicineId1 | 1 | 1 |
| medicineId2 | 2 | 2 |
| medicineId3 | 3 | 3 |
| treatmentId | 1 | 1 |

---

## 📝 POSTMAN REQUEST SETUP (Step by Step)

### 🔹 Request 1: Health Check

**Postman Setup:**
- **Method:** GET
- **URL:** `{{baseUrl}}/actuator/health`
- **Headers:** None
- **Body:** None

**Expected Response:**
```json
{
    "status": "UP"
}
```

---

### 🔹 Request 2: Create Patient

**Postman Setup:**
- **Method:** POST
- **URL:** `{{baseUrl}}/api/patients`
- **Headers:**
  ```
  Content-Type: application/json
  ```
- **Body → Raw → JSON:**
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

**Tests Tab (Add this script):**
```javascript
pm.test("Status code is 201", function () {
    pm.response.to.have.status(201);
});

pm.test("Patient created successfully", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('patientId');
    pm.collectionVariables.set("patientId", jsonData.patientId);
});
```

**Expected Response:**
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

### 🔹 Request 3: Create Medicine 1 (Metformin)

**Postman Setup:**
- **Method:** POST
- **URL:** `{{baseUrl}}/api/medicines`
- **Headers:**
  ```
  Content-Type: application/json
  ```
- **Body → Raw → JSON:**
  ```json
  {
      "name": "Metformin",
      "usage": "Diabetes management - helps control blood sugar levels",
      "sideEffect": "Nausea, diarrhea, metallic taste"
  }
  ```

**Tests Tab:**
```javascript
pm.test("Status code is 201", function () {
    pm.response.to.have.status(201);
});

pm.test("Medicine created successfully", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('medicineId');
    pm.collectionVariables.set("medicineId1", jsonData.medicineId);
});
```

---

### 🔹 Request 4: Create Medicine 2 (Insulin)

**Postman Setup:**
- **Method:** POST
- **URL:** `{{baseUrl}}/api/medicines`
- **Headers:**
  ```
  Content-Type: application/json
  ```
- **Body → Raw → JSON:**
  ```json
  {
      "name": "Insulin",
      "usage": "Blood glucose control for diabetes",
      "sideEffect": "Hypoglycemia, injection site reactions"
  }
  ```

**Tests Tab:**
```javascript
pm.test("Status code is 201", function () {
    pm.response.to.have.status(201);
});

pm.test("Medicine created successfully", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('medicineId');
    pm.collectionVariables.set("medicineId2", jsonData.medicineId);
});
```

---

### 🔹 Request 5: Create Medicine 3 (Lisinopril)

**Postman Setup:**
- **Method:** POST
- **URL:** `{{baseUrl}}/api/medicines`
- **Headers:**
  ```
  Content-Type: application/json
  ```
- **Body → Raw → JSON:**
  ```json
  {
      "name": "Lisinopril",
      "usage": "Blood pressure control",
      "sideEffect": "Dry cough, dizziness, fatigue"
  }
  ```

**Tests Tab:**
```javascript
pm.test("Status code is 201", function () {
    pm.response.to.have.status(201);
});

pm.test("Medicine created successfully", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('medicineId');
    pm.collectionVariables.set("medicineId3", jsonData.medicineId);
});
```

---

### 🔹 Request 6: Create Treatment Plan (MAIN API)

**Postman Setup:**
- **Method:** POST
- **URL:** `{{baseUrl}}/api/patients/{{patientId}}/treatment-plans`
- **Headers:**
  ```
  Content-Type: application/json
  ```
- **Body → Raw → JSON:**
  ```json
  {
      "doctorId": 456,
      "diseaseId": 789,
      "status": "ongoing",
      "notes": "Patient diagnosed with Type 2 diabetes. Monitor blood glucose levels daily. Follow strict diabetic diet and exercise routine. Regular check-ups every 3 months.",
      "medicines": [
          {
              "medicineId": {{medicineId1}},
              "dosage": "500mg",
              "timing": "Twice daily with meals"
          },
          {
              "medicineId": {{medicineId2}},
              "dosage": "10 units",
              "timing": "Before breakfast"
          }
      ]
  }
  ```

**Tests Tab:**
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Treatment plan created successfully", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('treatmentId');
    pm.expect(jsonData).to.have.property('medicines');
    pm.expect(jsonData.medicines).to.be.an('array');
    pm.expect(jsonData.medicines.length).to.be.above(0);
    pm.collectionVariables.set("treatmentId", jsonData.treatmentId);
});

pm.test("Medicine names are resolved", function () {
    var jsonData = pm.response.json();
    jsonData.medicines.forEach(function(medicine) {
        pm.expect(medicine.medicineName).to.not.be.null;
        pm.expect(medicine).to.have.property('dosage');
        pm.expect(medicine).to.have.property('timing');
    });
});
```

**Expected Response:**
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

---

### 🔹 Request 7: Get Treatment Plans

**Postman Setup:**
- **Method:** GET
- **URL:** `{{baseUrl}}/api/patients/{{patientId}}/treatment-plans`
- **Headers:** None
- **Body:** None

**Tests Tab:**
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response is an array", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.be.an('array');
});

pm.test("Treatment plans have required fields", function () {
    var jsonData = pm.response.json();
    if (jsonData.length > 0) {
        pm.expect(jsonData[0]).to.have.property('treatmentId');
        pm.expect(jsonData[0]).to.have.property('medicines');
    }
});
```

---

### 🔹 Request 8: Create Complex Treatment Plan

**Postman Setup:**
- **Method:** POST
- **URL:** `{{baseUrl}}/api/patients/{{patientId}}/treatment-plans`
- **Headers:**
  ```
  Content-Type: application/json
  ```
- **Body → Raw → JSON:**
  ```json
  {
      "doctorId": 789,
      "diseaseId": 123,
      "status": "ongoing",
      "notes": "Patient has comorbid conditions: Type 2 diabetes and hypertension. Requires comprehensive treatment plan with lifestyle modifications.",
      "medicines": [
          {
              "medicineId": {{medicineId1}},
              "dosage": "500mg",
              "timing": "Twice daily with breakfast and dinner"
          },
          {
              "medicineId": {{medicineId2}},
              "dosage": "15 units",
              "timing": "Before breakfast"
          },
          {
              "medicineId": {{medicineId3}},
              "dosage": "10mg",
              "timing": "Once daily in the morning"
          }
      ]
  }
  ```

---

### 🔹 Request 9: Test Patient Not Found (Error Test)

**Postman Setup:**
- **Method:** POST
- **URL:** `{{baseUrl}}/api/patients/999/treatment-plans`
- **Headers:**
  ```
  Content-Type: application/json
  ```
- **Body → Raw → JSON:**
  ```json
  {
      "doctorId": 456,
      "diseaseId": 789,
      "status": "ongoing",
      "notes": "This should fail - patient doesn't exist",
      "medicines": []
  }
  ```

**Tests Tab:**
```javascript
pm.test("Status code is 400", function () {
    pm.response.to.have.status(400);
});

pm.test("Error message is correct", function () {
    pm.expect(pm.response.text()).to.include("Patient not found");
});
```

---

### 🔹 Request 10: Test Invalid Medicine ID

**Postman Setup:**
- **Method:** POST
- **URL:** `{{baseUrl}}/api/patients/{{patientId}}/treatment-plans`
- **Headers:**
  ```
  Content-Type: application/json
  ```
- **Body → Raw → JSON:**
  ```json
  {
      "doctorId": 456,
      "diseaseId": 789,
      "status": "ongoing",
      "notes": "Testing with invalid medicine ID - should create but medicine name will be null",
      "medicines": [
          {
              "medicineId": 999,
              "dosage": "500mg",
              "timing": "Twice daily"
          }
      ]
  }
  ```

**Tests Tab:**
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Treatment created with null medicine name", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('treatmentId');
    pm.expect(jsonData.medicines[0].medicineName).to.be.null;
});
```

---

## 🏃‍♂️ COLLECTION RUNNER SETUP

### How to Run All Tests:
1. Click on your collection name
2. Click "Run collection" 
3. Select all requests
4. Set iterations: 1
5. Set delay: 1000ms (to avoid overwhelming server)
6. Click "Run HealSync Treatment Plan API"

### Expected Results:
- ✅ All status code tests pass
- ✅ Treatment plans created successfully  
- ✅ Medicine names resolved correctly
- ✅ Error handling works properly
- ✅ Variables set automatically for reuse

---

## 📊 ADDITIONAL HELPFUL REQUESTS

### Get All Patients
- **Method:** GET
- **URL:** `{{baseUrl}}/api/patients`

### Get All Medicines  
- **Method:** GET
- **URL:** `{{baseUrl}}/api/medicines`

### Get Specific Patient
- **Method:** GET
- **URL:** `{{baseUrl}}/api/patients/{{patientId}}`

### Get Specific Medicine
- **Method:** GET
- **URL:** `{{baseUrl}}/api/medicines/{{medicineId1}}`

---

## 🎯 SUCCESS INDICATORS

After running all requests, you should see:

✅ **Backend responds successfully**
✅ **Patient created with ID**
✅ **3 medicines created with IDs**
✅ **Treatment plans created with resolved medicine names**
✅ **Error handling works for invalid data**
✅ **All tests pass in Collection Runner**
✅ **Variables automatically set for reuse**

**Your Treatment Plan API is fully tested and working! 🎉**

---

## 🔧 PRO TIPS

1. **Use Collection Variables** - They auto-update as you create data
2. **Run Health Check First** - Backend might need time to wake up
3. **Check Tests Tab** - Green tests mean everything works
4. **Use Collection Runner** - Tests all scenarios at once
5. **Save Responses** - Good for documentation and debugging
