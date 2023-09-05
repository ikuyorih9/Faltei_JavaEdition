SRC := ArvoreB/src/ArvoreB
CLASSES := ArvoreB/classes

all: compile run

compile:
	javac -d $(CLASSES) $(SRC)/MainArvore.java $(SRC)/ArquivoDados.java $(SRC)/Disciplina.java $(SRC)/Iostream.java $(SRC)/Log.java
run:
	java -cp $(CLASSES) ArvoreB.MainArvore ArvoreB.ArquivoDados ArvoreB.Disciplina ArvoreB.Iostream ArvoreB.Log