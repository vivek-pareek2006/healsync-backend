# 🚀 Quick Postman Reference - Treatment Plan API

## 🏁 START HERE - Step by Step

### Step 1: Start Backend Server
```bash
cd c:\Users\vivek\healsync-backend
.\gradlew bootRun
```
Wait for: "Started HealsyncApplication" message

### Step 2: Test Server is Running
```
GET http://localhost:8080/actuator/health
```
Expected: `{"status":"UP"}`

---

## 📋 REQUIRED TEST DATA (Run these first)

### 1. Create Patient
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
**Save the patientId from response!**

### 2. Create Medicine 1
```
POST http://localhost:8080/api/medicines
Content-Type: application/json

{
    "name": "Metformin",
    "usage": "Diabetes management",
    "sideEffect": "Nausea, diarrhea"
}
```

### 3. Create Medicine 2
```
POST http://localhost:8080/api/medicines
Content-Type: application/json

{
    "name": "Insulin",
    "usage": "Blood glucose control",
    "sideEffect": "Hypoglycemia"
}
```

---

## 🎯 MAIN API TESTS

### ✅ TEST 1: Create Treatment Plan (SUCCESS)
```
POST http://localhost:8080/api/patients/1/treatment-plans
Content-Type: application/json

{
    "doctorId": 456,
    "diseaseId": 789,
    "status": "ongoing",
    "notes": "Patient has Type 2 diabetes. Monitor blood glucose levels daily.",
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
    "notes": "Patient has Type 2 diabetes. Monitor blood glucose levels daily.",
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

### ✅ TEST 2: Get Patient's Treatment Plans
```
GET http://localhost:8080/api/patients/1/treatment-plans
```

**Expected Response (200 OK):**
Array of treatment plans with medicines

---

## ❌ ERROR TESTS

### TEST 3: Patient Not Found (400 Error)
```
POST http://localhost:8080/api/patients/999/treatment-plans
Content-Type: application/json

{
    "doctorId": 456,
    "diseaseId": 789,
    "status": "ongoing",
    "notes": "This should fail",
    "medicines": []
}
```

**Expected Response:** `400 Bad Request` with message "Patient not found"

### TEST 4: Invalid Medicine ID (Partial Success)
```
POST http://localhost:8080/api/patients/1/treatment-plans
Content-Type: application/json

{
    "doctorId": 456,
    "diseaseId": 789,
    "status": "ongoing",
    "notes": "Testing invalid medicine ID",
    "medicines": [
        {
            "medicineId": 999,
            "dosage": "500mg",
            "timing": "Twice daily"
        }
    ]
}
```

**Expected:** Treatment plan created, but `medicineName` will be `null`

---

## 🔧 POSTMAN SETUP TIPS

### Collection Variables (Set these in Postman):
- `baseUrl`: `http://localhost:8080`
- `patientId`: `1` (use actual ID from patient creation)

### Headers for all POST requests:
```
Content-Type: application/json
```

### Quick Test Script (Add to Tests tab):
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response time is less than 2000ms", function () {
    pm.expect(pm.response.responseTime).to.be.below(2000);
});
```

---

## 🎯 VALIDATION CHECKLIST

✅ Server is running (port 8080)
✅ Patient created successfully (note the ID)
✅ Medicines created successfully (note the IDs)
✅ Treatment plan created with correct response structure
✅ Medicine names are resolved (not null)
✅ Error handling works for invalid patient ID
✅ GET endpoint returns treatment plans

---

## 🐛 COMMON ISSUES & FIXES

### ❌ "Connection refused"
**Fix:** Start backend server with `.\gradlew bootRun`

### ❌ "404 Not Found"
**Fix:** Check URL spelling - correct path is `/api/patients/{id}/treatment-plans`

### ❌ "Patient not found"
**Fix:** Use actual patientId from patient creation response

### ❌ "500 Internal Server Error"
**Fix:** Check backend console logs for detailed error

### ❌ Medicine name is null
**Fix:** Use valid medicineId from medicine creation response

---

## 📊 WHAT TO EXPECT

1. **Successful Creation:** Returns complete treatment plan with resolved medicine names
2. **Database Records:** Creates 1 TreatmentPlan + N TreatmentMedicine records
3. **Validation:** Patient must exist, medicines can be invalid (name becomes null)
4. **Transaction Safety:** All-or-nothing - if anything fails, nothing is saved

**Happy Testing! 🎉**
