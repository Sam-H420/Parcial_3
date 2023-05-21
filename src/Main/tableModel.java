package Main;

public class tableModel{
    public int id;
    public String name, gender;
    public Double grades;
    public String status;

    public tableModel(int id, String name, String gender, Double grades) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.grades = grades;
        
        if (grades >= 60) {
            this.status = "Pass";
        } else {
            this.status = "Fail";
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Double getGrades() {
        return grades;
    }

    public void setGrades(Double grades) {
        this.grades = grades;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
