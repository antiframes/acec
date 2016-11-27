package acec.antiframes.carteirinha;

class User {
    private String name;
    private String cpf;
    private String cnpj;
    private String occupation;
    private String dueDate;
    private String company;
    private String picUrl;

    User(){}

    public User(String name, String cpf, String cnpj, String occupation, String dueDate, String company, String picUrl) {
        this.name = name;
        this.cpf = cpf;
        this.cnpj = cnpj;
        this.occupation = occupation;
        this.dueDate = dueDate;
        this.company = company;
        this.picUrl = picUrl;
    }

    String getPicUrl() {
        return picUrl;
    }

    void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    String getCompany() {
        return company;
    }

    void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getCpf() {
        return cpf;
    }

    void setCpf(String cpf) {
        this.cpf = cpf;
    }

    String getCnpj() {
        return cnpj;
    }

    void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    String getOccupation() {
        return occupation;
    }

    void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    String getDueDate() {
        return dueDate;
    }

    void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    boolean isValid(){
        return (name!=null &&
                cpf!=null &&
                dueDate!=null &&
                picUrl!=null);
    }
}
