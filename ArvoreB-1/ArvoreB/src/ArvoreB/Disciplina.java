package ArvoreB;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Disciplina {
    private String nomeDisciplina;
    private String nomeProfessor;
    private int corEscolhida;
    private int quantidadeAulas;
    private int quantidadeLimiteFaltas;
    private int quantidadeCreditos;
    private ArrayList <Date> faltas;

    public Disciplina(String nomeDisciplina, String nomeProfessor, int corEscolhida, int quantidadeCreditos){
        this.nomeDisciplina = nomeDisciplina;
        this.nomeProfessor = nomeProfessor;
        this.corEscolhida = corEscolhida;
        this.quantidadeCreditos = quantidadeCreditos;
        this.quantidadeAulas = (quantidadeCreditos*MainArvore.horasPorCredito*60)/100;
        faltas = new ArrayList<>();
        quantidadeLimiteFaltas = quantidadeAulas - ((int)((MainArvore.mediaFaltas) * quantidadeAulas)+1);
    }

    public Disciplina(){
        faltas = new ArrayList<>();
        quantidadeLimiteFaltas = 0;
    }

    public void setNomeDisciplina(String nomeDisciplina){
        this.nomeDisciplina = nomeDisciplina;
    }
    public void setNomeProfessor(String nomeProfessor){
        this.nomeProfessor = nomeProfessor;
    }
    public void setCorEscolhida(int corEscolhida){
        this.corEscolhida = corEscolhida;
    }

    public void setQuantidadeAulas(int quantidadeAulas){
        this.quantidadeAulas = quantidadeAulas;
    }
    public void setQuantidadeCreditos(int quantidadeCreditos){
        this.quantidadeCreditos = quantidadeCreditos;
        this.quantidadeAulas = (quantidadeCreditos*MainArvore.horasPorCredito*60)/100;
    }
    public void setLimiteFaltas(int quantidadeLimiteFaltas){
        this.quantidadeLimiteFaltas = quantidadeLimiteFaltas;
    }
    public void setFaltas(ArrayList<Date> faltas){
        this.faltas = faltas;
    }
    
    public void adicionarFaltaOrdenado(Date data){
        int size = faltas.size();
        for(int i = 0; i < size; i++){
            if(data.compareTo(faltas.get(i)) <= 0) {
                faltas.add(i, data);
                return;
            }
        }
        faltas.add(data);
        quantidadeLimiteFaltas--;
    }
    public void adicionarFalta(Date data){
        faltas.add(data);
        quantidadeLimiteFaltas --;
    }
    public void removerFalta(Date data){
        faltas.remove(data);
        quantidadeLimiteFaltas++;
    }
    public void removerFalta(int index){
        faltas.remove(index);
    }
    public String getNomeDisciplina(){
        return nomeDisciplina;
    }
    public String getNomeProfessor(){
        return nomeProfessor;
    }
    public int getCorEscolhida(){
        return corEscolhida;
    }
    public int getQuantidadeAulas(){
        calculaQuantidadeAulas();
        return quantidadeAulas;
    }
    public int getQuantidadeFaltas(){
        return faltas.size();
    }
    public Date getFalta(int i){
        return faltas.get(i);
    }
    public int getQuantidadeLimiteFaltas(){
        calculaLimiteFaltas();
        return quantidadeLimiteFaltas;
    }
    public ArrayList <Date> getFaltas(){
        return faltas;
    }
    public double getPercentualFaltas(){
        return ((double) faltas.size())/quantidadeAulas;
    }
    public int getQuantidadeCreditos(){
        return quantidadeCreditos;
    }
    public boolean equalDisciplina(Disciplina disciplina){
        boolean eqNome = this.nomeDisciplina == disciplina.getNomeDisciplina();
        boolean eqProf = this.nomeProfessor == disciplina.getNomeProfessor();
        boolean eqCor = this.corEscolhida == disciplina.getCorEscolhida();

        if(eqNome && eqProf && eqCor)
            return true;

        return false;
    }

    public ArrayList <String> listaDataFaltas(){
        ArrayList <String> dataFaltas = new ArrayList<>();
        for(int i = 0; i <faltas.size(); i++){
            Date data = faltas.get(i);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            dataFaltas.add(dateFormat.format(data));
        }
        return dataFaltas;
    }


    public void calculaQuantidadeAulas(){
        quantidadeAulas = (quantidadeCreditos*MainArvore.horasPorCredito*60)/100;
    }

    public void calculaLimiteFaltas(){
        calculaQuantidadeAulas();
        quantidadeLimiteFaltas = quantidadeAulas - ((int)((MainArvore.mediaFaltas) * quantidadeAulas)+1);
    }

    public int idFaltaPorString(String falta){
        for(int i = 0; i < faltas.size(); i++){
            if(faltas.get(i).toString().compareTo(falta) == 0)
                return i;
        }
        return -1;
    }
}
