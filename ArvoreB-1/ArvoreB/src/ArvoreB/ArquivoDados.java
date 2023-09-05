package ArvoreB;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ArquivoDados{
	public static final int TAM_PAG = 4096;
	public static final byte LIXO_CHAR = 7;
	private String nome;
	private String diretorio;
	public Cabecalho cabecalho;
	private Iostream stream;

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
			arquivoConsistente(false);
			stream.write(proxRRN, 1);
			stream.write(ultimoExcluidoRRN, 5);
			stream.write(quantidadeRegistros, 9);
			arquivoConsistente(true);
			insereLixo(TAM_DADOS_CAB, TAM_PAG);
		}

		private void arquivoConsistente(boolean consistencia){
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
			Log.d("HomeActivity", "Status do arquivo: " + status);
			Log.d("HomeActivity", "Próximo RRN disponível: " + proxRRN);
			Log.d("HomeActivity", "RRN do último registro excluído: " + ultimoExcluidoRRN);
			Log.d("HomeActivity", "Quantidade de registros: " + quantidadeRegistros);

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
			nomeArquivoFaltas = "faltas_" + RRN + ".txt";
		}

		public void insereRegistroArquivo(){
			int offset = ArquivoDados.calculaOffset(RRN);
			int offsetInicio = offset;

			cabecalho.arquivoConsistente(false);

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

			cabecalho.arquivoConsistente(true);
		}

		public void adicionaFaltas(Date data){
			File faltas = new File(diretorio, nomeArquivoFaltas);
			try{
				RandomAccessFile streamFaltas = new RandomAccessFile(faltas, "rw");
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				streamFaltas.seek(streamFaltas.length());
				streamFaltas.writeChars(dateFormat.format(data) + '\n');
				streamFaltas.close();
			}
			catch(IOException e){
				Log.e("HomeActivity", e.getLocalizedMessage());
			}

		}

		public void imprimeRegistro(){
			Log.d("HomeActivity", "Nome da disciplina: " + disciplina.getNomeDisciplina());
			Log.d("HomeActivity", "Nome do professor: " + disciplina.getNomeProfessor());
			Log.d("HomeActivity", "Cor da disciplina: " + disciplina.getCorEscolhida());
			Log.d("HomeActivity", "Quantidade de créditos: " + disciplina.getQuantidadeCreditos());
			Log.d("HomeActivity", "Nome do arquivo de faltas " + nomeArquivoFaltas);
		}
	
	}	

	public void insereLixo(int from, int to){
		if(stream == null)
			return;
		for(int i = from; i < to; i++)
			stream.write(LIXO_CHAR, i);

	}

	public void insereDisciplina(Disciplina disciplina){
		int RRN;
		if(cabecalho.ultimoExcluidoRRN == -1)
			RRN = cabecalho.proxRRN;
		else{
			RRN = cabecalho.ultimoExcluidoRRN;
			int offset = calculaOffset(RRN) + 1;
			cabecalho.ultimoExcluidoRRN = stream.iRead(offset);
		}

		Registro registro = new Registro(disciplina, RRN);
		registro.insereRegistroArquivo();
		cabecalho.proxRRN++;
		cabecalho.quantidadeRegistros++;
		cabecalho.atualizaCabecalhoArquivo();
	}

	public Registro recuperaRegistro(int RRN){
		int offset = ArquivoDados.calculaOffset(RRN);

		char removido = stream.cRead(offset);
		if(removido == '1'){
			Log.d("HomeActivity", "Registro de RRN " + RRN + " já estava removido. Não foi possível recuperá-lo.");
			return null;
		}
		
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
		String nomeArquivoFaltas = stream.sRead(offset+4,tamanho);
		offset += tamanho+4;

		Disciplina disciplina = new Disciplina(nomeDisciplina, nomeProfessor, cor, creditos);
		return new Registro(disciplina, RRN);
	}

	public void excluiRegistro(int RRN){
		int offset = calculaOffset(RRN);
		char removido = stream.cRead(offset);
		if(removido == '1'){
			Log.d("HomeActivity", "Registro já está removido!");
			return;
		}

		cabecalho.arquivoConsistente(false);

		stream.write((byte)'1', offset);
		offset++;
		
		int ultimoExcluido = cabecalho.ultimoExcluidoRRN;
		stream.write(ultimoExcluido, offset);
		cabecalho.ultimoExcluidoRRN = RRN;
		cabecalho.quantidadeRegistros--;

		cabecalho.arquivoConsistente(false);
		cabecalho.atualizaCabecalhoArquivo();
	}

	public static int calculaOffset(int RRN){
		return ArquivoDados.TAM_PAG + (RRN * Registro.TAM_REGISTRO);
	}
}