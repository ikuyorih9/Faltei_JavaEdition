package ArvoreB;

import ArvoreB.ArquivoDados.Cabecalho;
import ArvoreB.ArquivoDados.Registro;

public class MainArvore{
	public static double mediaFaltas = 0.7;
  public static int horasPorCredito = 15;
	
	public static void main(String[] args) {
		Disciplina disciplina = new Disciplina("nomeDisciplina", "nomeProfessor", 0, 4);
    	ArquivoDados arq = new ArquivoDados(null, "teste.bin");
		
		ArquivoDados.Registro reg = arq.new Registro(disciplina);
		reg.insereRegistroArquivo(ArquivoDados.TAM_PAG);

		Disciplina simoes = new Disciplina("Organização e Arquitetura de Computadores", "Eduardo do Valle Simões", 0, 4);
		ArquivoDados.Registro reg2 = arq.new Registro(simoes);
		reg2.insereRegistroArquivo(ArquivoDados.TAM_PAG + Registro.TAM_REGISTRO);

		reg2.leRegistroArquivo(ArquivoDados.TAM_PAG + Registro.TAM_REGISTRO);
		reg2.imprimeRegistro();
  	}
}