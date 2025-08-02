# 🔄 Treatment Plan System: Database Operations & Frontend Workflows

## 🗃️ Database Operations Breakdown

### 1. **Treatment Plan Creation - Step by Step**

```sql
-- Step 1: Validate Patient Exists
SELECT patientId, patientName FROM Patient WHERE patientId = 123;
-- Result: John Doe (ID: 123) ✅

-- Step 2: Create Treatment Plan Record
INSERT INTO TreatmentPlan (patientId, doctorId, diseaseId, status, startDate, notes)
VALUES (123, 456, 789, 'ongoing', '2025-08-01', 'Monitor blood glucose daily');
-- Generated treatmentId: 999 ✅

-- Step 3: Create Medicine Prescriptions
-- Medicine 1: Metformin
INSERT INTO TreatmentMedicine (treatmentID, medicineName, dosage, timing)
VALUES (999, 'Metformin', '500mg', 'Twice daily with meals');
-- Generated treatmentMedID: 1001 ✅

-- Medicine 2: Insulin  
INSERT INTO TreatmentMedicine (treatmentID, medicineName, dosage, timing)
VALUES (999, 'Insulin', '10 units', 'Before breakfast');
-- Generated treatmentMedID: 1002 ✅

-- Step 4: Fetch Complete Treatment Plan for Response
SELECT 
    tp.treatmentId, tp.patientId, tp.doctorId, tp.diseaseId, 
    tp.status, tp.startDate, tp.notes,
    tm.treatmentMedID, tm.medicineName, tm.dosage, tm.timing
FROM TreatmentPlan tp
LEFT JOIN TreatmentMedicine tm ON tp.treatmentId = tm.treatmentID
WHERE tp.treatmentId = 999;
```

## 🎨 Frontend Implementation Details

### 1. **Doctor's Treatment Plan Creation Interface**

```tsx
import React, { useState, useEffect } from 'react';

interface Medicine {
  medicineId: number;
  dosage: string;
  timing: string;
}

interface TreatmentPlanForm {
  patientId: number;
  doctorId: number;
  diseaseId: number;
  status: string;
  notes: string;
  medicines: Medicine[];
}

const CreateTreatmentPlan: React.FC = () => {
  const [patients, setPatients] = useState([]);
  const [diseases, setDiseases] = useState([]);
  const [medicines, setMedicines] = useState([]);
  const [formData, setFormData] = useState<TreatmentPlanForm>({
    patientId: 0,
    doctorId: getCurrentDoctorId(), // From auth context
    diseaseId: 0,
    status: 'ongoing',
    notes: '',
    medicines: []
  });

  // Load dropdown data
  useEffect(() => {
    loadPatients();
    loadDiseases();
    loadMedicines();
  }, []);

  const loadPatients = async () => {
    const response = await fetch('/api/patients');
    setPatients(await response.json());
  };

  const loadDiseases = async () => {
    const response = await fetch('/api/diseases');
    setDiseases(await response.json());
  };

  const loadMedicines = async () => {
    const response = await fetch('/api/medicines');
    setMedicines(await response.json());
  };

  const addMedicine = () => {
    setFormData(prev => ({
      ...prev,
      medicines: [...prev.medicines, { medicineId: 0, dosage: '', timing: '' }]
    }));
  };

  const updateMedicine = (index: number, field: keyof Medicine, value: string | number) => {
    setFormData(prev => ({
      ...prev,
      medicines: prev.medicines.map((med, i) => 
        i === index ? { ...med, [field]: value } : med
      )
    }));
  };

  const removeMedicine = (index: number) => {
    setFormData(prev => ({
      ...prev,
      medicines: prev.medicines.filter((_, i) => i !== index)
    }));
  };

  const submitTreatmentPlan = async () => {
    try {
      const response = await fetch(`/api/patients/${formData.patientId}/treatment-plans`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData)
      });
      
      if (response.ok) {
        const result = await response.json();
        alert(`Treatment Plan created successfully! ID: ${result.treatmentId}`);
        // Reset form or redirect
      } else {
        const error = await response.text();
        alert(`Error: ${error}`);
      }
    } catch (err) {
      alert('Failed to create treatment plan');
    }
  };

  return (
    <div className="treatment-plan-form">
      <h2>Create Treatment Plan</h2>
      
      {/* Patient Selection */}
      <div className="form-group">
        <label>Patient:</label>
        <select 
          value={formData.patientId} 
          onChange={(e) => setFormData(prev => ({...prev, patientId: parseInt(e.target.value)}))}
        >
          <option value={0}>Select Patient</option>
          {patients.map(patient => (
            <option key={patient.patientId} value={patient.patientId}>
              {patient.patientName} (Age: {patient.patientAge})
            </option>
          ))}
        </select>
      </div>

      {/* Disease Selection */}
      <div className="form-group">
        <label>Disease/Condition:</label>
        <select 
          value={formData.diseaseId}
          onChange={(e) => setFormData(prev => ({...prev, diseaseId: parseInt(e.target.value)}))}
        >
          <option value={0}>Select Disease</option>
          {diseases.map(disease => (
            <option key={disease.diseaseId} value={disease.diseaseId}>
              {disease.diseaseName}
            </option>
          ))}
        </select>
      </div>

      {/* Status */}
      <div className="form-group">
        <label>Status:</label>
        <select 
          value={formData.status}
          onChange={(e) => setFormData(prev => ({...prev, status: e.target.value}))}
        >
          <option value="ongoing">Ongoing</option>
          <option value="completed">Completed</option>
          <option value="paused">Paused</option>
          <option value="cancelled">Cancelled</option>
        </select>
      </div>

      {/* Notes */}
      <div className="form-group">
        <label>Treatment Notes:</label>
        <textarea 
          value={formData.notes}
          onChange={(e) => setFormData(prev => ({...prev, notes: e.target.value}))}
          placeholder="Enter treatment instructions, monitoring requirements, etc."
          rows={4}
        />
      </div>

      {/* Medicines Section */}
      <div className="medicines-section">
        <h3>Prescribed Medicines</h3>
        {formData.medicines.map((medicine, index) => (
          <div key={index} className="medicine-row">
            <select 
              value={medicine.medicineId}
              onChange={(e) => updateMedicine(index, 'medicineId', parseInt(e.target.value))}
            >
              <option value={0}>Select Medicine</option>
              {medicines.map(med => (
                <option key={med.medicineId} value={med.medicineId}>
                  {med.name}
                </option>
              ))}
            </select>
            
            <input 
              type="text"
              placeholder="Dosage (e.g., 500mg)"
              value={medicine.dosage}
              onChange={(e) => updateMedicine(index, 'dosage', e.target.value)}
            />
            
            <input 
              type="text"
              placeholder="Timing (e.g., Twice daily with meals)"
              value={medicine.timing}
              onChange={(e) => updateMedicine(index, 'timing', e.target.value)}
            />
            
            <button onClick={() => removeMedicine(index)}>Remove</button>
          </div>
        ))}
        
        <button onClick={addMedicine}>Add Medicine</button>
      </div>

      {/* Submit */}
      <button 
        onClick={submitTreatmentPlan}
        disabled={!formData.patientId || !formData.diseaseId || formData.medicines.length === 0}
      >
        Create Treatment Plan
      </button>
    </div>
  );
};
```

### 2. **Patient's Treatment Plan View Interface**

```tsx
interface TreatmentPlanResponse {
  treatmentId: number;
  patientId: number;
  doctorId: number;
  diseaseId: number;
  status: string;
  startDate: string;
  notes: string;
  medicines: {
    treatmentMedID: number;
    medicineName: string;
    dosage: string;
    timing: string;
  }[];
}

const PatientTreatmentPlans: React.FC = () => {
  const [treatmentPlans, setTreatmentPlans] = useState<TreatmentPlanResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const patientId = getCurrentPatientId(); // From auth context

  useEffect(() => {
    loadTreatmentPlans();
  }, []);

  const loadTreatmentPlans = async () => {
    try {
      const response = await fetch(`/api/patients/${patientId}/treatment-plans`);
      const plans = await response.json();
      setTreatmentPlans(plans);
    } catch (err) {
      console.error('Failed to load treatment plans');
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div>Loading your treatment plans...</div>;

  return (
    <div className="patient-dashboard">
      <h2>My Treatment Plans</h2>
      
      {treatmentPlans.length === 0 ? (
        <p>No active treatment plans found.</p>
      ) : (
        <div className="treatment-plans-grid">
          {treatmentPlans.map(plan => (
            <div key={plan.treatmentId} className="treatment-card">
              <div className="treatment-header">
                <h3>Treatment Plan #{plan.treatmentId}</h3>
                <span className={`status-badge ${plan.status}`}>
                  {plan.status.toUpperCase()}
                </span>
              </div>
              
              <div className="treatment-info">
                <p><strong>Start Date:</strong> {plan.startDate}</p>
                <p><strong>Doctor ID:</strong> {plan.doctorId}</p>
                <p><strong>Condition:</strong> Disease ID {plan.diseaseId}</p>
              </div>
              
              {plan.notes && (
                <div className="treatment-notes">
                  <h4>Doctor's Notes:</h4>
                  <p>{plan.notes}</p>
                </div>
              )}
              
              <div className="medicines-list">
                <h4>Prescribed Medicines:</h4>
                {plan.medicines.map(medicine => (
                  <div key={medicine.treatmentMedID} className="medicine-card">
                    <div className="medicine-header">
                      <h5>{medicine.medicineName}</h5>
                    </div>
                    <div className="medicine-details">
                      <p><strong>Dosage:</strong> {medicine.dosage}</p>
                      <p><strong>Timing:</strong> {medicine.timing}</p>
                    </div>
                    <div className="medicine-actions">
                      <button>Set Reminder</button>
                      <button>Mark as Taken</button>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};
```

## 🔄 Data Flow Scenarios

### **Scenario 1: Doctor Creates Treatment Plan**

```
1. Frontend Action:
   Doctor fills form → Selects Patient → Chooses Disease → Adds Medicines

2. API Call:
   POST /api/patients/123/treatment-plans
   {
     "doctorId": 456,
     "diseaseId": 789,
     "status": "ongoing", 
     "notes": "Monitor daily",
     "medicines": [
       {"medicineId": 101, "dosage": "500mg", "timing": "Twice daily"}
     ]
   }

3. Backend Processing:
   Validate Patient → Create TreatmentPlan → Create TreatmentMedicines → Return Response

4. Frontend Response:
   Success: Show confirmation + redirect to treatment details
   Error: Show error message + highlight invalid fields
```

### **Scenario 2: Patient Views Treatment Plans**

```
1. Frontend Action:
   Patient logs in → Navigates to "My Treatments"

2. API Call:
   GET /api/patients/123/treatment-plans

3. Backend Processing:
   Fetch all TreatmentPlans for patient → Join with TreatmentMedicines → Return complete data

4. Frontend Display:
   Render treatment cards → Show medicines with dosages → Enable reminders
```

### **Scenario 3: Medicine Reminder System**

```
1. Frontend Logic:
   Parse medicine timing → Calculate next dose time → Set browser notification

2. Example Timing Processing:
   "Twice daily with meals" → [08:00, 20:00]
   "Before breakfast" → [07:30]
   "Every 6 hours" → [06:00, 12:00, 18:00, 00:00]

3. Notification System:
   Browser push notifications → Email reminders → SMS alerts
```

## 🎯 Key Benefits of This Implementation

### **For Doctors:**
- ✅ Comprehensive patient management
- ✅ Structured prescription creation
- ✅ Treatment progress tracking
- ✅ Integration with patient records

### **For Patients:**
- ✅ Clear medicine schedules
- ✅ Automated reminders
- ✅ Treatment history access
- ✅ Doctor communication channel

### **For System:**
- ✅ Complete audit trail
- ✅ Data integrity maintenance
- ✅ Scalable architecture
- ✅ API-first design

This implementation creates a comprehensive healthcare management system where each entity has a clear purpose and all relationships are properly maintained through the database and reflected in the user interfaces.
