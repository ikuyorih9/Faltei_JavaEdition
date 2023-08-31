package arvoreB;

public class Registro{
    String nomeDisciplina;
    String nomeProfessor;

    public Registro(String nomeDisciplina, String nomeProfessor){
        this.nomeDisciplina = nomeDisciplina;
        this.nomeProfessor = nomeProfessor;
    }

    private class Cabecalho{
        int proxRRN;
        int ultimoRemovidoRRN;
    }
}