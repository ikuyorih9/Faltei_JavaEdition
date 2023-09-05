package ArvoreB;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Disciplina {
    //Toda vez que as configurações forem salvas, é preciso recalcular o número de aulas e o limite de faltas.
    public static double mediaFaltas = 0.7;
  	public static int horasPorCredito = 15;

    private String nomeDisciplina;
    private String nomeProfessor;
    private int corEscolhida;
    private int quantidadeAulas;
    private int quantidadeLimiteFaltas;
    private int quantidadeCreditos;
    private ArrayList <Date> faltas;

    /**Construtor da disciplina. Aqui, além de definido as características básicas da disciplina, também é calculado a quantidade de aulas e o limite máximo de faltas na disciplina.
     * 
     * @param nomeDisciplina String - nome da disciplina.
     * @param nomeProfessor String - nome do professor da disciplina.
     * @param corEscolhida int - cor escolhida para a disciplina.
     * @param quantidadeCreditos int - quantidade de créditos-aula da disciplina.
     */
    public Disciplina(String nomeDisciplina, String nomeProfessor, int corEscolhida, int quantidadeCreditos){
        this.nomeDisciplina = nomeDisciplina;
        this.nomeProfessor = nomeProfessor;
        this.corEscolhida = corEscolhida;
        this.quantidadeCreditos = quantidadeCreditos;
        calculaQuantidadeAulas();
        calculaLimiteFaltas();
        faltas = new ArrayList<>();
    }

    public Disciplina(){
        faltas = new ArrayList<>();
        quantidadeLimiteFaltas = 0;
    }

    /*-----------------------------
    |   FUNÇÕES DE CADASTRO (SET)  |
    ------------------------------*/

    /**Insere o nome da disciplina.
     * 
     * @param nomeDisciplina String - nome da disciplina.
     */
    public void setNomeDisciplina(String nomeDisciplina){
        this.nomeDisciplina = nomeDisciplina;
    }

    /**Insere o nome do professor da disciplina.
     * 
     * @param nomeProfessor String - nome do professor da disciplina.
     */
    public void setNomeProfessor(String nomeProfessor){
        this.nomeProfessor = nomeProfessor;
    }
    
    /**Insere a cor escolhida para a disciplina. A cor é um número de quatro bytes, onde cada byte é formado por um aspecto da cor, em RGB.
     * @param corEscolhida int - cor escolhida.
     */
    public void setCorEscolhida(int corEscolhida){
        this.corEscolhida = corEscolhida;
    }

    /**Insere a quantidade de créditos-aula da disciplina. Com o número de créditos, calcula-se, também, a quantidade de aulas, segundo a taxa de horas por crédito. Sempre que a quantidade de créditos-aula mudar, é preciso recalcular a quantidade de aulas e o limite máximo de faltas.
     * @param quantidadeCreditos int - quantidade de créditos-aula.
     */
    public void setQuantidadeCreditos(int quantidadeCreditos){
        this.quantidadeCreditos = quantidadeCreditos;
        calculaQuantidadeAulas();
        calculaLimiteFaltas();
    }

    /**Insere o array de faltas na disciplina pela data da falta.
     * 
     * @param faltas ArrayList<Date> - array de datas dos dias de falta na aula da disciplina.
     */
    public void setFaltas(ArrayList<Date> faltas){
        this.faltas = faltas;
    }
    
    /*--------------------------------------
    |   FUNÇÕES DE PROCESSAMENTO DE FALTAS  |
    ---------------------------------------*/

    /**Adiciona a data de uma falta, ordenadamente, ao array de faltas.
     * 
     * @param data Date - data da falta.
     */
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
    
    /**Adiciona a data de uma falta na última posição do array.
     * 
     * @param data Date - data da falta.
     */
    public void adicionarFalta(Date data){
        faltas.add(data);
        quantidadeLimiteFaltas --;
    }

    /**Remove uma falta do array pela data da falta.
     * 
     * @param data Date - data da falta a se remover.
     */
    public void removerFalta(Date data){
        faltas.remove(data);
        quantidadeLimiteFaltas++;
    }

    /**Remove uma falta do array pela posição da falta no array.
     * 
     * @param index int - posição da falta no array.
     */
    public void removerFalta(int index){
        faltas.remove(index);
    }

    /*------------------------------
    |   FUNÇÕES DE RECUPERAÇÃO (GET)    | 
    -------------------------------*/

    /**Recupera o nome da disciplina.
     * 
     * @return String - nome da disciplina.
     */
    public String getNomeDisciplina(){
        return nomeDisciplina;
    }

    /**Recupera o nome do professor da disciplina.
     * 
     * @return String - nome do professor da disciplina.
     */
    public String getNomeProfessor(){
        return nomeProfessor;
    }

    /**Recupera o inteiro da cor escolhida para a disciplina.
     * 
     * @return int - cor escolhida para a disciplina.
     */
    public int getCorEscolhida(){
        return corEscolhida;
    }

    /**Recupera a quantidade de aulas da disciplina.
     * 
     * @return int - quantidade de aulas da disciplina.
     */
    public int getQuantidadeAulas(){
        return quantidadeAulas;
    }

    /**Recupera a quantidade de faltas regitradas na disciplina.
     * 
     * @return int - quantidade de faltas na disciplina.
     */
    public int getQuantidadeFaltas(){
        return faltas.size();
    }

    /**Recupera a data de uma falta, na posição i do array de faltas.
     * 
     * @param i int - posição da falta no array de faltas.
     * @return Date - data da falta na posição i.
     */
    public Date getFalta(int i){
        return faltas.get(i);
    }

    /**Recupera o limite máximo de faltas na disciplina.
     * 
     * @return int - limite máximo de faltas na disciplina.
     */
    public int getQuantidadeLimiteFaltas(){
        return quantidadeLimiteFaltas;
    }

    /**Recupera o array de faltas regitradas da disciplina.
     * 
     * @return ArrayList <Date> - Array das datas de faltas da disciplina.
     */
    public ArrayList <Date> getFaltas(){
        return faltas;
    }

    /**Calcula o percentual de faltas na disciplina.
     * 
     * @return Double - percentual de faltas na disciplina.
     */
    public double getPercentualFaltas(){
        return ((double) faltas.size())/quantidadeAulas;
    }

    /**Recupera a quantidade de créditos-aula da disciplina.
     * 
     * @return int - quantidade de créditos-aula da disciplina.
     */
    public int getQuantidadeCreditos(){
        return quantidadeCreditos;
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

    /*----------------------------------------------------
    |   FUNÇÕES DE OBTENÇÃO DE DADOS A PARTIR DO CÁLCULO  | 
    -----------------------------------------------------*/

    /**Calcula a quantidade de aulas que uma disciplina tem, a partir da quantidade de créditos-aula e das horas por crédito da disciplina. Épreciso que a quantidade de créditos-aula e as horas por crédito estejam atualizadas.*/
    public void calculaQuantidadeAulas(){
        quantidadeAulas = (quantidadeCreditos*horasPorCredito*60)/100;
    }

    /**Calcula o limite máximo de faltas a partir da quantidade de aulas e a média de faltas da disciplina. É preciso que a quantidade de aulas esteja atualizada.*/
    public void calculaLimiteFaltas(){
        quantidadeLimiteFaltas = quantidadeAulas - ((int)((mediaFaltas) * quantidadeAulas)+1);
    }

    public int idFaltaPorString(String falta){
        for(int i = 0; i < faltas.size(); i++){
            if(faltas.get(i).toString().compareTo(falta) == 0)
                return i;
        }
        return -1;
    }
}
