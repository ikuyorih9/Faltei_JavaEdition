package ArvoreB;

import java.util.Date;
import java.util.GregorianCalendar;

import ArvoreB.ArquivoDados.Cabecalho;
import ArvoreB.ArquivoDados.Registro;

public class MainArvore{
	public static double mediaFaltas = 0.7;
  	public static int horasPorCredito = 15;
	
	public static void main(String[] args) {
		ArquivoDados arq = new ArquivoDados(null, "teste.bin");

		Disciplina disciplina = new Disciplina("nomeDisciplina", "nomeProfessor", 0, 4);
		Disciplina simoes = new Disciplina("Organização e Arquitetura de Computadores", "Eduardo do Valle Simões", 0, 4);
		Disciplina a = new Disciplina("Jubileu de ouro", "Jubilosvaldo", 0, 4);

		arq.insereDisciplina(disciplina);
		arq.insereDisciplina(simoes);
		arq.insereDisciplina(a);

		arq.excluiRegistro(1);
		arq.excluiRegistro(2);

		arq.insereDisciplina(a);
		arq.insereDisciplina(simoes);
  	}
}