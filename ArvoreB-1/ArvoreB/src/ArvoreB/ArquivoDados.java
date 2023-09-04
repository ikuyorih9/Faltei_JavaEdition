package ArvoreB;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

import javax.sound.sampled.SourceDataLine;

public class ArquivoDados{
	public static final int TAM_PAG = 4096;
	public static final byte LIXO_CHAR = 7;
	String nome;
	String diretorio;
	Cabecalho cabecalho;
	Iostream stream;

	public ArquivoDados(String diretorio, String nome){
		this.nome = nome;
		this.diretorio = diretorio;
		stream = new Iostream(diretorio, nome);
		cabecalho = new Cabecalho();		
	}

	public class Cabecalho{
		public static final int TAM_DADOS_CAB = 13;
		private char status;
		protected int proxRRN;
		protected int ultimoExcluidoRRN;
		protected int quantidadeRegistros;

		public Cabecalho(){
			status = '0';
			proxRRN = 0;
			ultimoExcluidoRRN = -1;
			quantidadeRegistros = 0;
			atualizaCabecalhoArquivo();
		}

		public void atualizaCabecalhoArquivo(){
			Iostream stream = new Iostream(diretorio, nome);
			arquivoConsistente(false, stream);
			stream.write(proxRRN, 1);
			stream.write(ultimoExcluidoRRN, 5);
			stream.write(quantidadeRegistros, 9);
			arquivoConsistente(true, stream);
			insereLixo(TAM_DADOS_CAB, TAM_PAG);
		}

		private void arquivoConsistente(boolean consistencia, Iostream stream){
			if(consistencia == false)
				status = '0';
			else
				status = '1';
			stream.write((byte)status, 0);
		}
		
		public void recuperaCabecalho(){
			Iostream stream = new Iostream(diretorio, nome);
			status = stream.cRead(0);
			proxRRN = stream.iRead(1);
			ultimoExcluidoRRN = stream.iRead(5);
			imprimeCabecalho();
		}

		public void imprimeCabecalho(){
			System.out.println("Status: " + status);
			System.out.println("proxRRN: " + proxRRN);
			System.out.println("ultimoExcluidoRRN: " + ultimoExcluidoRRN);
		}
	}

	public class Registro{
		public static final int TAM_REGISTRO = 256;
		private int RRN;
		private Disciplina disciplina;
		private String nomeArquivoFaltas;

		public Registro(Disciplina disciplina, int RRN){
			this.disciplina = disciplina;
			this.RRN = RRN;
			nomeArquivoFaltas = "faltas_" + RRN + ".bin";
		}

		public void insereRegistroArquivo(){
			int offset = ArquivoDados.calculaOffset(RRN);
			int offsetInicio = offset;

			char removido = '0';
			stream.write((byte)removido, offset);

			offset++;

			String nomeDisciplina = disciplina.getNomeDisciplina();
			stream.write(nomeDisciplina.length(),offset);
			stream.write(nomeDisciplina, offset+4);

			offset += nomeDisciplina.length() + 4;

			String nomeProfessor = disciplina.getNomeProfessor();
			stream.write(nomeProfessor.length(), offset);
			stream.write(nomeProfessor, offset+4);

			offset += nomeProfessor.length() + 4;

			int corDisciplina = disciplina.getCorEscolhida();
			stream.write(corDisciplina, offset);

			offset += 4;

			int quatidadeCreditos = disciplina.getQuantidadeCreditos();
			stream.write(quatidadeCreditos, offset);

			offset+=4;

			stream.write(nomeArquivoFaltas.length(), offset);
			stream.write(nomeArquivoFaltas, offset + 4);
			offset += nomeArquivoFaltas.length() + 4;

			insereLixo(offset, offsetInicio + TAM_REGISTRO);
		}

		public void leRegistroArquivo(){
			int offset = ArquivoDados.calculaOffset(RRN);

			char removido = stream.cRead(offset);
			if(removido == '1')
				return;
			
			offset++;

			int tamanho = stream.iRead(offset);
			String nomeDisciplina = stream.sRead(offset+4, tamanho);
			offset += tamanho + 4;

			tamanho = stream.iRead(offset);
			String nomeProfessor = stream.sRead(offset+4, tamanho);
			offset += tamanho + 4;

			int cor = stream.iRead(offset);
			offset+=4;

			int creditos = stream.iRead(offset);
			offset+=4;
			
			tamanho = stream.iRead(offset);
			nomeArquivoFaltas = stream.sRead(offset+4,tamanho);
			offset += tamanho+4;

			disciplina = new Disciplina(nomeDisciplina, nomeProfessor, cor, creditos);
			System.out.println("arquivo: " + nomeArquivoFaltas);
		}
	
		public void atualizaArquivoFaltas(){
			File faltas = new File(diretorio, nomeArquivoFaltas);

		}

		public void imprimeRegistro(){
			System.out.println("Nome da disciplina: " + disciplina.getNomeDisciplina());
			System.out.println("Nome do professor: " + disciplina.getNomeProfessor());
			System.out.println("Cor da disciplina: " + disciplina.getCorEscolhida());
			System.out.println("Quantidade de créditos: " + disciplina.getQuantidadeCreditos());
		}
	
	}	

	public void insereLixo(int from, int to){
		if(stream == null)
			return;
		for(int i = from; i < to; i++)
			stream.write(LIXO_CHAR, i);

	}

	public void insereDisciplina(Disciplina disciplina){
		Registro registro = new Registro(disciplina, cabecalho.proxRRN);
		registro.insereRegistroArquivo();
		cabecalho.proxRRN++;
		cabecalho.quantidadeRegistros++;
		cabecalho.atualizaCabecalhoArquivo();
	}

	public static int calculaOffset(int RRN){
		return ArquivoDados.TAM_PAG + (RRN * Registro.TAM_REGISTRO);
	}
}