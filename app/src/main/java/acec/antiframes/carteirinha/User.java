package acec.antiframes.carteirinha;

public class User {
    private String name;
    private String cpf;
    private String cnpj;
    private String userType;
    private String dueDate;
    private String company;
    private String picUrl;

    public User(){}

    public User(String name, String cpf, String cnpj, String userType, String dueDate, String company,String picUrl) {
        this.name = name;
        this.cpf = cpf;
        this.cnpj = cnpj;
        this.userType = userType;
        this.dueDate = dueDate;
        this.company = company;
        this.picUrl = picUrl;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
