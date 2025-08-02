# 🏥 Treatment Plan & Medicine System Visualization

## 📊 Database Schema Diagram

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│     Patient     │    │  TreatmentPlan  │    │    Medicine     │
├─────────────────┤    ├─────────────────┤    ├─────────────────┤
│ patientId (PK)  │◄──┤ treatmentId(PK) │    │ medicineId (PK) │
│ patientName     │   │ patientId (FK)  │    │ name            │
│ patientAge      │   │ doctorId        │    │ usage           │
│ gender          │   │ diseaseId       │    │ sideEffect      │
│ mobileNo        │   │ status          │    │                 │
│ email           │   │ startDate       │    │                 │
│ password        │   │ notes           │    │                 │
│                 │   │ bill            │    │                 │
└─────────────────┘   └─────────────────┘    └─────────────────┘
                             │
                             │ One-to-Many
                             ▼
                    ┌─────────────────┐
                    │TreatmentMedicine│
                    ├─────────────────┤
                    │treatmentMedID(PK)│
                    │ treatmentID(FK) │
                    │ medicineName    │
                    │ dosage          │
                    │ timing          │
                    │ usageInfo       │
                    │ sideEffect      │
                    └─────────────────┘
```

## 🔄 API Flow Visualization

### 1. Create Treatment Plan Flow

```
┌─────────────┐    POST /api/patients/123/treatment-plans
│   Doctor    │ ─────────────────────────────────────────────┐
│             │                                              │
└─────────────┘                                              ▼
                    ┌─────────────────────────────────────────────┐
                    │         TreatmentPlanController             │
                    │ ┌─────────────────────────────────────────┐ │
                    │ │ 1. Receive Request                      │ │
                    │ │    - patientId: 123                     │ │
                    │ │    - doctorId: 456                      │ │
                    │ │    - diseaseId: 789                     │ │
                    │ │    - status: "ongoing"                  │ │
                    │ │    - medicines: [...]                   │ │
                    │ └─────────────────────────────────────────┘ │
                    └─────────────────────────────────────────────┘
                                         │
                                         ▼
                    ┌─────────────────────────────────────────────┐
                    │         TreatmentPlanService                │
                    │ ┌─────────────────────────────────────────┐ │
                    │ │ 2. Validate Patient Exists              │ │
                    │ │    patientRepository.findById(123)      │ │
                    │ └─────────────────────────────────────────┘ │
                    │ ┌─────────────────────────────────────────┐ │
                    │ │ 3. Create TreatmentPlan Entity          │ │
                    │ │    - Link to Patient                    │ │
                    │ │    - Set doctorId, diseaseId, status    │ │
                    │ │    - Auto-set startDate = today         │ │
                    │ └─────────────────────────────────────────┘ │
                    │ ┌─────────────────────────────────────────┐ │
                    │ │ 4. Save TreatmentPlan                   │ │
                    │ │    treatmentPlanRepository.save()       │ │
                    │ │    → Get treatmentId = 999              │ │
                    │ └─────────────────────────────────────────┘ │
                    │ ┌─────────────────────────────────────────┐ │
                    │ │ 5. Loop Through Medicines               │ │
                    │ │    For each medicine in request:        │ │
                    │ │    - Create TreatmentMedicine           │ │
                    │ │    - Link to treatmentId: 999           │ │
                    │ │    - Set dosage, timing                 │ │
                    │ │    - Fetch medicine name by ID          │ │
                    │ │    - Save TreatmentMedicine             │ │
                    │ └─────────────────────────────────────────┘ │
                    └─────────────────────────────────────────────┘
                                         │
                                         ▼
                    ┌─────────────────────────────────────────────┐
                    │            Response                         │
                    │ ┌─────────────────────────────────────────┐ │
                    │ │ 6. Return Complete Treatment Plan       │ │
                    │ │    - treatmentId: 999                   │ │
                    │ │    - All patient, doctor, disease info  │ │
                    │ │    - List of all prescribed medicines   │ │
                    │ │    - Full dosage and timing details     │ │
                    │ └─────────────────────────────────────────┘ │
                    └─────────────────────────────────────────────┘
```

## 📝 Data Flow Example

### Input Request:
```json
POST /api/patients/123/treatment-plans
{
    "doctorId": 456,
    "diseaseId": 789,
    "status": "ongoing",
    "notes": "Patient has diabetes. Monitor blood sugar daily.",
    "medicines": [
        {
            "medicineId": 101,
            "dosage": "500mg",
            "timing": "Twice daily with meals"
        },
        {
            "medicineId": 102,
            "dosage": "10 units",
            "timing": "Before breakfast"
        }
    ]
}
```

### Database Operations:

```
1. INSERT INTO TreatmentPlan:
   ┌─────────────┬──────────┬─────────┬───────────┬──────────┬─────────────┬───────────────────────────────────────┐
   │ treatmentId │ patientId│ doctorId│ diseaseId │ status   │ startDate   │ notes                                 │
   ├─────────────┼──────────┼─────────┼───────────┼──────────┼─────────────┼───────────────────────────────────────┤
   │ 999         │ 123      │ 456     │ 789       │ ongoing  │ 2025-08-01  │ Patient has diabetes. Monitor blood...│
   └─────────────┴──────────┴─────────┴───────────┴──────────┴─────────────┴───────────────────────────────────────┘

2. INSERT INTO TreatmentMedicine (Record 1):
   ┌──────────────┬─────────────┬──────────────┬─────────┬──────────────────────────┐
   │treatmentMedID│ treatmentID │ medicineName │ dosage  │ timing                   │
   ├──────────────┼─────────────┼──────────────┼─────────┼──────────────────────────┤
   │ 1001         │ 999         │ Metformin    │ 500mg   │ Twice daily with meals   │
   └──────────────┴─────────────┴──────────────┴─────────┴──────────────────────────┘

3. INSERT INTO TreatmentMedicine (Record 2):
   ┌──────────────┬─────────────┬──────────────┬─────────┬──────────────────────────┐
   │treatmentMedID│ treatmentID │ medicineName │ dosage  │ timing                   │
   ├──────────────┼─────────────┼──────────────┼─────────┼──────────────────────────┤
   │ 1002         │ 999         │ Insulin      │ 10 units│ Before breakfast         │
   └──────────────┴─────────────┴──────────────┴─────────┴──────────────────────────┘
```

## 🔗 Relationship Visualization

```
Patient (ID: 123)
│
├── Treatment Plan (ID: 999)
│   ├── Doctor: Dr. Smith (ID: 456)
│   ├── Disease: Diabetes Type 2 (ID: 789)
│   ├── Status: ongoing
│   ├── Start Date: 2025-08-01
│   ├── Notes: "Monitor blood sugar daily..."
│   │
│   └── Medicines Prescribed:
│       ├── Medicine 1 (ID: 1001)
│       │   ├── Name: Metformin
│       │   ├── Dosage: 500mg
│       │   └── Timing: Twice daily with meals
│       │
│       └── Medicine 2 (ID: 1002)
│           ├── Name: Insulin
│           ├── Dosage: 10 units
│           └── Timing: Before breakfast
```

## ⚡ Key System Features

### 🔒 Transactional Safety
```
BEGIN TRANSACTION
├── Validate Patient Exists
├── Create Treatment Plan
├── Save Treatment Plan → Get ID
├── Create Medicine 1 → Link to Treatment Plan
├── Create Medicine 2 → Link to Treatment Plan
├── Create Medicine N → Link to Treatment Plan
└── COMMIT (All succeed) or ROLLBACK (Any fails)
```

### 🔍 Data Validation
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Patient ID    │    │   Medicine ID   │    │  Doctor/Disease │
│     Check       │    │     Check       │    │     IDs         │
├─────────────────┤    ├─────────────────┤    ├─────────────────┤
│ ✅ Exists       │    │ ✅ Exists       │    │ ⚠️  Not Validated│
│ ❌ Not Found    │    │ ⚠️  Name = null │    │    (Trusted)    │
│ → 400 Error    │    │    if not found │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### 📱 API Response Structure
```json
{
    "treatmentId": 999,
    "patientId": 123,
    "doctorId": 456,
    "diseaseId": 789,
    "status": "ongoing",
    "startDate": "2025-08-01",
    "notes": "Patient has diabetes. Monitor blood sugar daily.",
    "medicines": [
        {
            "treatmentMedID": 1001,
            "medicineName": "Metformin",
            "dosage": "500mg",
            "timing": "Twice daily with meals"
        },
        {
            "treatmentMedID": 1002,
            "medicineName": "Insulin",
            "dosage": "10 units",
            "timing": "Before breakfast"
        }
    ]
}
```

## 🎯 Use Cases

### 1. **Doctor Creates Treatment Plan**
- Doctor selects patient
- Diagnoses condition (diseaseId)
- Prescribes medicines with specific dosages
- Adds treatment notes and instructions
- System creates linked records

### 2. **Patient Views Treatment Plan**
- Patient can see their active treatment plans
- View all prescribed medicines
- See dosage instructions and timing
- Read doctor's notes

### 3. **Pharmacy Fulfills Prescription**
- Access treatment medicine records
- Verify prescribed dosages
- Dispense correct amounts
- Track medicine distribution

### 4. **Treatment Monitoring**
- Track treatment status (ongoing, completed, paused)
- Monitor patient compliance
- Update treatment plans as needed
- Calculate treatment costs

## 🚀 System Benefits

✅ **Comprehensive Medical Records**
✅ **Structured Medicine Management**
✅ **Doctor-Patient-Medicine Linkage**
✅ **Audit Trail for Treatments**
✅ **Scalable for Multiple Conditions**
✅ **API-First Design for Integration**
