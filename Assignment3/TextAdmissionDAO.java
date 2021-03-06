package Assignment3;

import java.util.ArrayList;

/**
 * implementation of IAdmissionDAO using a Text file database
 */
public class TextAdmissionDAO implements IAdmissionDAO {

    private final ArrayList<String> initialAdmissionList;
    private ArrayList<Admission> admissions;

    TextAdmissionDAO(){

        ReadFiles reader = new ReadFiles("Assignment3/admission.txt");
        this.initialAdmissionList = reader.getListFormat();
        this.admissions = new ArrayList<Admission>();
        initializeAdmissions();
    }

    @Override
    public Admission createAdmission(int admissionID, int patientID) {
        for(Admission admission: admissions){
            if(admission.getPatientID() == patientID){
                System.out.println("You already created an admission for patient " + patientID +
                        "\n\tAdmission ID: " + admission.getAdmissionID());
                return null;
            }
        }
        Admission newAdmission = new Admission(admissionID,patientID);
        admissions.add(newAdmission);
        return newAdmission;
    }

    @Override
    public boolean deleteAdmission(int admissionID) {
        for(Admission admission: admissions){
            if(admission.getAdmissionID() == admissionID){
                admissions.remove(admission);
                return true;
            }
        }
        return false;
    }

    @Override
    public void addExamination(int admissionID, String type, String[] operations) {
        Admission patient = findAdmission(admissionID);
        if (patient != null) {
            patient.addExamination(type, operations);
        }else{
            System.out.println("Error while adding examination:\n" +
                    "\tNo such patient with admission ID " + admissionID);
        }
    }

    @Override
    public String totalCost(int admissionID) {
        Admission patient = findAdmission(admissionID);
        if(patient != null){
            return patient.totalCost();
        }
        else return "TotalCost for admission " + admissionID + " cannot be called.";
    }

    @Override
    public ArrayList<Admission> getAdmissions() {
        return admissions;
    }

    public void removedPatientUpdate(int patientID){
        Admission toRemove = null;
        for(Admission admission: admissions){
            if(admission.getPatientID() == patientID){
                toRemove = admission;
                System.out.println("Admission of patient " + patientID +
                        " removed because this patient has removed from the patient database");
            }
        }
        //will do nothing if it's null (if the patient already has no admissions)
        admissions.remove(toRemove);
    }

    /**
     * this takes the initially given file and create Admission objects of it
     */
    private void initializeAdmissions(){
        Admission temp = null;
        for(String info: initialAdmissionList){
            String[] infoSplit = info.split("\t");
            if(isNumeric(infoSplit[0])){
                temp = createAdmission(
                        Integer.parseInt(infoSplit[0].trim()),//admissionID
                        Integer.parseInt(infoSplit[1].trim())//patientID
                );//createAdmission
            }else{
                if (temp == null) System.out.println("Your admission file is not in correct format.");
                else temp.addExamination(infoSplit[0], infoSplit[1]);
            }
        }
    }

    /**
     * to find the respective admission
     * @param admissionID given
     * @return the admission
     */
    private Admission findAdmission(int admissionID){
        for(Admission admission: admissions){
            if(admissionID == admission.getAdmissionID()){
                return admission;
            }
        }
        System.out.println("Patient with admission ID " + admissionID + " cannot be found.");
        return null;
    }

    //this is a helper method to see the admissionID's from file and tell if a new admission needs to be created
    private boolean isNumeric(String value){
        try{
            Integer.valueOf(value);
            return true;
        }catch (NumberFormatException n){
            return false;
        }
    }
}
