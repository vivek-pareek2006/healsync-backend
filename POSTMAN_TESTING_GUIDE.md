# 📮 Complete Postman Testing Guide for Treatment Plan API

## 🚀 Step 1: Start Your HealSync Backend Server

### Option A: Using Gradle
```bash
# Navigate to your project directory
cd c:\Users\vivek\healsync-backend

# Start the application using Gradle
.\gradlew bootRun
```

### Option B: Using IDE
```bash
# Run HealsyncApplication.java main method in your IDE
# Server will start on default port 8080
```

**Verify Server is Running:**
- Open browser and go to: `http://localhost:8080`
- You should see Spring Boot startup logs or a default page

## 📋 Step 2: Prepare Test Data (Prerequisites)

### 2.1 Create a Patient (Required for Treatment Plan)

**Request:**
```
POST http://localhost:8080/api/patients
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

### 2.2 Create Medicines (Required for Prescriptions)

**Medicine 1:**
```
POST http://localhost:8080/api/medicines
Content-Type: application/json

{
    "name": "Metformin",
    "usage": "Diabetes management",
    "sideEffect": "Nausea, diarrhea, metallic taste"
}
```

**Medicine 2:**
```
POST http://localhost:8080/api/medicines
Content-Type: application/json

{
    "name": "Insulin",
    "usage": "Blood glucose control",
    "sideEffect": "Hypoglycemia, injection site reactions"
}
```

## 🎯 Step 3: Postman Collection Setup

### 3.1 Create New Collection
1. Open Postman
2. Click "New" → "Collection"
3. Name it: "HealSync Treatment Plan API"
4. Add description: "Testing Treatment Plan and Medicine APIs"

### 3.2 Set Collection Variables
1. Go to Collection → Variables tab
2. Add these variables:

| Variable | Initial Value | Current Value |
|----------|---------------|---------------|
| baseUrl | http://localhost:8080 | http://localhost:8080 |
| patientId | 1 | 1 |
| doctorId | 1 | 1 |
| diseaseId | 1 | 1 |
| medicineId1 | 1 | 1 |
| medicineId2 | 2 | 2 |

## 🧪 Step 4: Main Treatment Plan API Tests

### Test 1: Create Treatment Plan ✅

**Setup in Postman:**
1. **Method:** POST
2. **URL:** `{{baseUrl}}/api/patients/{{patientId}}/treatment-plans`
3. **Headers:**
   ```
   Content-Type: application/json
   ```
4. **Body (raw JSON):**
   ```json
   {
       "doctorId": 456,
       "diseaseId": 789,
       "status": "ongoing",
       "notes": "Patient has Type 2 diabetes. Monitor blood glucose levels daily. Follow strict diabetic diet and exercise routine.",
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
    "notes": "Patient has Type 2 diabetes. Monitor blood glucose levels daily. Follow strict diabetic diet and exercise routine.",
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

### Test 2: Get Patient's Treatment Plans ✅

**Setup in Postman:**
1. **Method:** GET
2. **URL:** `{{baseUrl}}/api/patients/{{patientId}}/treatment-plans`
3. **Headers:** None required

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
        "notes": "Patient has Type 2 diabetes...",
        "medicines": [...]
    }
]
```

## 🔧 Step 5: Error Testing Scenarios

### Test 3: Patient Not Found ❌

**Setup:**
1. **Method:** POST
2. **URL:** `{{baseUrl}}/api/patients/999/treatment-plans`
3. **Body:** Same as Test 1

**Expected Response (400 Bad Request):**
```
Patient not found
```

### Test 4: Invalid Medicine ID ⚠️

**Setup:**
1. **Method:** POST
2. **URL:** `{{baseUrl}}/api/patients/{{patientId}}/treatment-plans`
3. **Body:**
   ```json
   {
       "doctorId": 456,
       "diseaseId": 789,
       "status": "ongoing",
       "notes": "Test with invalid medicine",
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

### Test 5: Missing Required Fields ❌

**Setup:**
1. **Method:** POST
2. **URL:** `{{baseUrl}}/api/patients/{{patientId}}/treatment-plans`
3. **Body:**
   ```json
   {
       "status": "ongoing",
       "medicines": []
   }
   ```

**Expected Response (400 Bad Request):**
```
Validation error or constraint violation
```

## 📊 Step 6: Advanced Testing Scenarios

### Test 6: Multiple Medicines Treatment Plan

**Body:**
```json
{
    "doctorId": 456,
    "diseaseId": 789,
    "status": "ongoing",
    "notes": "Complex treatment plan with multiple medications",
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
        },
        {
            "medicineId": 1,
            "dosage": "250mg",
            "timing": "Before bedtime"
        }
    ]
}
```

### Test 7: Empty Medicines Array

**Body:**
```json
{
    "doctorId": 456,
    "diseaseId": 789,
    "status": "ongoing",
    "notes": "Treatment plan without medicines - lifestyle changes only",
    "medicines": []
}
```

### Test 8: Different Status Values

**Test with each status:**
- `"ongoing"`
- `"completed"`
- `"paused"`
- `"cancelled"`

## 🎯 Step 7: Postman Test Scripts

### Add to Test 1 (Create Treatment Plan):

**Tests Tab:**
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response has treatmentId", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('treatmentId');
    pm.expect(jsonData.treatmentId).to.be.a('number');
});

pm.test("Response contains medicines", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('medicines');
    pm.expect(jsonData.medicines).to.be.an('array');
    pm.expect(jsonData.medicines.length).to.be.above(0);
});

pm.test("Medicine names are resolved", function () {
    var jsonData = pm.response.json();
    jsonData.medicines.forEach(function(medicine) {
        pm.expect(medicine.medicineName).to.not.be.null;
        pm.expect(medicine.medicineName).to.be.a('string');
    });
});

// Save treatmentId for future tests
if (pm.response.code === 200) {
    var responseData = pm.response.json();
    pm.collectionVariables.set("treatmentId", responseData.treatmentId);
}
```

### Add to Test 3 (Patient Not Found):

**Tests Tab:**
```javascript
pm.test("Status code is 400", function () {
    pm.response.to.have.status(400);
});

pm.test("Error message is correct", function () {
    pm.expect(pm.response.text()).to.include("Patient not found");
});
```

## 🔄 Step 8: Running Tests in Sequence

### Create Test Sequence:
1. **Prerequisites:** Create Patient → Create Medicines
2. **Main Tests:** Create Treatment Plan → Get Treatment Plans
3. **Error Tests:** Invalid Patient → Invalid Data
4. **Edge Cases:** Empty medicines → Multiple medicines

### Postman Collection Runner:
1. Click on collection
2. Click "Run collection"
3. Select all requests
4. Set iterations: 1
5. Click "Run HealSync Treatment Plan API"

## 📝 Step 9: Expected Database State After Tests

### TreatmentPlan Table:
```
treatmentId | patientId | doctorId | diseaseId | status  | startDate  | notes
1          | 1         | 456      | 789       | ongoing | 2025-08-01 | Patient has Type 2...
```

### TreatmentMedicine Table:
```
treatmentMedID | treatmentID | medicineName | dosage | timing
1             | 1           | Metformin    | 500mg  | Twice daily with meals
2             | 1           | Insulin      | 10 units| Before breakfast
```

## 🐛 Troubleshooting Common Issues

### Issue 1: Connection Refused
```
Solution: Ensure backend server is running on port 8080
Check: http://localhost:8080/actuator/health
```

### Issue 2: 404 Not Found
```
Solution: Verify URL path is correct
Correct: /api/patients/{patientId}/treatment-plans
```

### Issue 3: 500 Internal Server Error
```
Solution: Check backend logs for errors
Common causes: Missing repositories, database connection issues
```

### Issue 4: Foreign Key Constraint Errors
```
Solution: Ensure Patient exists before creating treatment plan
Use valid patientId from previous patient creation
```

## 🎉 Success Criteria

✅ **Treatment Plan Created Successfully**
✅ **Medicines Linked to Treatment Plan**
✅ **Medicine Names Resolved from Medicine Table**
✅ **Patient Validation Works**
✅ **Error Handling Functions Properly**
✅ **Response Structure Matches Expected Format**

Now you can systematically test your Treatment Plan API using this comprehensive Postman guide!
