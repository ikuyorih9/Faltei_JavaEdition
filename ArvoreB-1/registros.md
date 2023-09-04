# PLANEJAMENTO DA BASE DADOS DO FALTEI.

## O registro de cabeçalho.

O registro de cabeçalho é formado por 13 bytes (0 à 12), contendo:
* **Status (0)**: representa a consistência do arquivo e ocupa **1 byte**. Ao começar a escrita de dados no arquivo, o status é '0'. Ao terminar a escrita, o status é '1'.

* **Próximo RRN (1)**: apresenta o RRN (byte de localização) do próximo registro vazio, disponível para escrita. É um inteiro, portanto, ocupa **4 bytes**.

* **Último RRN excluido (5)**: apresenta o RRN (byte de localização) do último registro apagado, correspondendo ao topo da pilha de registros apagados. É um inteiro, portanto, ocupa **4 bytes**.

* **Quantidade de registros (9)**: apresenta a quantidade de registros presentes no arquivo. É um inteiro, portanto, ocupa **4 bytes**.

## O registro de dados.

O registro de dados possui tamanho fixo em **128 bytes**, apresentando os campos variáveis (strings) e fixos (char e inteiros):
* **Byte de remoção (char);**
* **Nome da disciplina (string);**
* **Nome do professor (string);**
* **Cor da disciplina (int);**
* **Quantidade de créditos (int).** 

Os campos variáveis apresentam **4 bytes** ao início para indicar o seu tamanho. Os espaços restantes ao fim do arquivo serão preenchidos com o número 7 (Bell).
