# Diário de programação!

**Por:** *Hugo Hiroyuki Nakamura.*

**Criado em:** *29 de julho de 2023.*

**Finalizado em:** *ainda em desenvolvimento.*

    Diário de criação do aplicativo "Faltei!". Todas (ou a maioria) das mudanças serão relatadas no diário.
---

## 29 de julho de 2023: Splash screen, Toolbar e Home.

Hoje, além do projeto ser iniciado, eu desenvolvi um **Splash Screen**, que consiste numa tela de apresentação, executada a cada vez que se entra no aplicativo. Por enquanto, ela apenas tem o nome "Faltei" (que ainda está em discussão) numa fonte e fundo quaisquer.

<p align="center">
    <img width = 400 src="diaryImages/29.07.23 - splashscreen.png">
</p>

A **Toolbar** foi o processo mais chato de se executar, tive que fazer um minicurso disponibilizado pela google e modificar um exemplo do Android Studio. A barra de ferramentas ainda está simples, mas pretendo colocar uma sub-barra lateral, para dar acesso as configurações e outras coisas.

Por fim, *eu não sou bom em design*, por isso a **Home** ficou bem defeituosa. Ela não tá o maior exemplo de beleza, mas até a versão final, ela deve melhorar (eu espero).
<p align="center">
    <img width = 400 src="diaryImages/29.07.23 - home.png">
</p>

## 31 de julho de 2023: tentativa de criar um menu lateral.

Hoje eu tentei criar um menu lateral. Eu copiei um outro exemplo, onde ele usa **navigators** e **fragments**. Os fragmentos são fáceis de entender, visto que eles são literalmente fragmentos da tela, onde você junta num layout só. Os navegadores, eu copiei o código, esperando entender. 

O problema da vez foi que, copiando o código do exemplo, o meu layout não disponibiliza o botão de abrir a barra lateral, ele troca pelo botão de voltar (navigate up). Eu entendi que o código não consegue entender que existe um top-level destionation.

Felizmente, depois de desistir, eu fiquei pensando no caminho lógico do código para carregar um fragmento. No exemplo do Android Studio, ao clicar em um tópico "Gallery" do menu lateral, o fragmento mostrado na tela era trocado por ele. Mas como o código sabe que, ao clicar em "Gallery", ele deve abrir o fragmento do "Gallery"?

 Isso fez eu vasculhar os códigos do *Faltei* que envolvem o menu lateral e os fragmentos de destino. Descobri que o XML do menu lateral e o XML do gráfico de navegação estavam com IDs diferentes. O código não estava entendendo que havia uma navegação a se seguir, então ele apenas colocava o símbolo de voltar.

<p align="center">
    <img width=150 src="diaryImages/31.07.23 - problema na navegacao.png">
    <img width=150 src="diaryImages/31.07.23 - navegacao concluida.png">
    <img width=150 src="diaryImages/31.07.23 - menu lateral.png">
</p>

## 1 de agosto de 2023: criação da tela de adicionar disciplina.

Hoje eu fiz um grande progresso: aprendi como navegar entre fragmentos. O único problema disso é que, talvez, uma abordagem melhor fosse criar Activities, em vez de trocar os fragmentos.

O que importa é que hoje eu finalmente criei uma tela sozinho: a tela de adicionar disciplinas.

<p align = "center">
    <img width=200 src="diaryImages/01.08.23 - tela de disciplinas.png">
    <img width=200 src="diaryImages/01.08.23 - tela de adicionar disciplina.png">
</p>

Basicamente, quando você clica no menu lateral em "Disciplinas", entra-se na tela da imagem à esquerda. Ao clicar em "+", entra-se na tela da imagem à direita, onde você pode inserir o nome da disciplina, o professor e uma cor.

Agora, é preciso descobrir como passar do "Adicionar Disciplina" para o "Disciplinas".

## 3 de agosto de 2023: navegar de volta e banner da disciplina.

Apesar de eu ter afirmado que aprendi a navegar entre fragmentos, eu fiquei algum tempo pensando como navegar de volta ao fragmento anterior.

Primeiro, eu descobri sobre o "FragmentManager" e seu método "BeginTransition". Basicamente, esse método apresenta o "replace", que realoca um outro fragmento. Entretanto, isso não é muito funcional para a navegação, pois ele simplesmente troca o fragmento, mas não atualiza o menu, e, inclusive, não apaga a fila de BackStacks (se clicar em voltar, ele volta o fragmento anterior).

No fim, era só recuperar o "NavController" da atividade principal e utilizar o método "PopBackStack".

```
NavHostFragment navHostFragment = (NavHostFragment) getActivity()
                        .getSupportFragmentManager()
                        .findFragmentById(R.id.nav_contentMain);
NavController navControl= navHostFragment.getNavController();
navControl.popBackStack();
```

Com a navegação de volta resolvida, é preciso saber como passar informação de um fragmento ao outro. Para isso, foi utilizado o "setFragmentResult" (sender) e o "setFragmentResultListener" (receiver). Funciona da seguinte forma: o setFragmentResult envia um *Bundle* para outro fragmento, e o listener detecta a chegada dele e realiza uma ação.

Um *Bundle* é um tipo de dado binário, relacionando uma chave com o produto da informação. Mas como mandar três informações (nome da disciplina, nome do professor e cor da disciplina) por um bundle? Para isso, cria-se uma classe Disciplina.

```
public class Disciplina implements Serializable {
    private String nomeDisciplina;
    private String nomeProfessor;
    private int corEscolhida;

    public void setMethods(){}
    public Type getMethods(){}
}
```

Como um Bundle não permite que uma classe seja associada, mas deixa um Serializable, então, aplica-se o *implements Serializable* e tudo funciona perfeitamente. Assim, envia-se esse Bundle,

```
Disciplina infoD = new Disciplina(disciplina, prof, corEscolhida);

Bundle infoClass = new Bundle();

infoClass.putSerializable("infoKey", infoD);

getParentFragmentManager().setFragmentResult("requestKey", infoClass);
```
o qual é recebido em:

```
getParentFragmentManager().setFragmentResultListener("requestKey", this, new FragmentResultListener() {
    @Override
    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
        FragmentAdicionarDisciplina.Disciplina disciplina = ((FragmentAdicionarDisciplina.Disciplina) result.getSerializable("infoKey"));
        criaBannerDisciplina(disciplina);

    }
});
```

Por fim, com as informações recebidas, basta criar um banner a partir de um template em outro layout. Para isso, tem-se

```
View banner = getLayoutInflater().inflate(R.layout.banner_disciplina, null, false);
```

que carrega o banner do layout template e pode-se aplicar ele no layout atual.

<p align = "center">
    <img width=200 src="diaryImages/03.08.23 - banner disciplina.png">
</p>

## 5 de agosto de 2023: finalização da tela de disciplinas.

A comunicação dos fragmentos, como abordado no último relato, foi abandonado. É preciso ter uma lista de todas as disciplinas salvas no aplicativo, para que outros fragmentos/atividades possam usar. Além disso, comunicar uma disciplina para outro fragmento era uma abordagem bem deselegante, é muito melhor deixar uma lista centralizada.

Dessa forma, na tela de adicionar disciplinas, o botão de adicionar apenas colocava a disciplina na lista.

Com uma lista de disciplinas centralizada, era preciso encontrar uma maneira de salvá-las na memória. Logo, foi utilizado o "SharedPreferences", responsável por salvar um binário (chave, objeto) na pasta principal do aplicativo. 

Na atividade principal, onde se situa a lista, a função "salvarDisciplinas" surge para salvar a lista inteira na memória. Observe que as disciplinas estão sendo salvas com as chaves "nomeDisciplina i", "nomeProfessor i", "corDisciplina i", onde i é a posição dela na lista.

```
public void salvarDisciplinas(){
    SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPref.edit();

    int size = disciplinasSalvas.size();
    editor.putInt("quantidadeDisciplinas", size);

    for(int i = 0; i < size; i++){
        Disciplina disciplina = disciplinasSalvas.get(i);
        String nomeDisciplina = disciplina.getNomeDisciplina();
        String nomeProfessor = disciplina.getNomeProfessor();
        int cor = disciplina.getCorEscolhida();

        editor.putString("nomeDisciplina " + i, nomeDisciplina);
        editor.putString("nomeProfessor " + i, nomeProfessor);
        editor.putInt("corDisciplina " + i, cor);
        editor.apply();
    }
}
```

Da mesma forma, a função "carregarDisciplinas" recupera as disciplinas salvas e as põe de volta na lista.

```
public void carregarDisciplinas(){
    SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

    int quantidadeDisciplinas = sharedPref.getInt("quantidadeDisciplinas", -1);

    for(int i = 0; i < quantidadeDisciplinas; i++){
        String nomeDisciplina = sharedPref.getString("nomeDisciplina " + i, "Disciplina -");
        String nomeProfessor = sharedPref.getString("nomeProfessor " + i, "Professor -");
        int cor = sharedPref.getInt("corDisciplina " + i, -1);

        Disciplina disciplina = new Disciplina(nomeDisciplina, nomeProfessor, cor);
        disciplinasSalvas.add(disciplina);
    }
}
```

Também, a função "apagarDisciplina" permite que uma disciplina seja deletada da lista e da memória.

```
public void apagarDisciplina(Disciplina disciplina){
    int iDisciplina = disciplinasSalvas.indexOf(disciplina);

    SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
    sharedPref.edit().remove("nomeDisciplina " + iDisciplina);

    disciplinasSalvas.remove(iDisciplina);
}
```

Toda vez que uma disciplina é adicionada, modificada ou apagada, a lista e a memória precisam ser atualizadas. Portanto, a função "salvarDisciplina" é requisitada várias vezes.

Por fim, uma opção de modificação da disciplina foi adicionada. Quando se clica na disciplina, a tela de adicionar abre novamente, mas com os botões atualizados.

<p align="center">
    <img width=200 src="diaryImages/05.08.23 - tela de edicao.png">
</p>

Para saber qual a disciplina selecionada para se modificar, deu para reutilizar a comunicação por "fragmentResult". Logo, antes de navegar a outra tela, tem-se o código abaixo.

```
Bundle bundleBannerDisciplina = new Bundle();

bundleBannerDisciplina.putSerializable("bundleBannerDisciplina", disciplina);

getParentFragmentManager().setFragmentResult("bundleDisciplina", bundleBannerDisciplina);
```

Há um erro a se corrigir ainda: ao desligar a tela do celular e ligá-la novamente, a tela de disciplinas duplica a lista. Isso acontece porque na função "onResume", ocorre a adição dos banners da lista, sem reconhecer os que já estão na tela.

## 6 de agosto de 2023: piecharts e gestão de faltas.

No último dia estive trabalhando numa forma de inserir gráficos no aplicativo. Para isso, eu descobri o MPAndroidChart, uma biblioteca que disponibiliza vários tipos de gráficos. A instalação é simples, basta abrir o build.gradle(app) e o settings.gradle() e adicionar as linhas

```
repositories {
    maven { url "https://jitpack.io" }
}
dependencies {
    implementation 'com.github.PhilJay:MPAndroidChart:v3.0.3'
}
```

Com os gráficos adicionados, foi possível criar as telas de estatística de faltas e a tela de adicionar. A tela de estatísticas mostra um gráfico geral sobre todas as faltas, dentre as aulas, além dos banners das disciplinas, mostrando o quanto de falta você tem em mini piecharts. Há um botão flutuante que leva a outra tela para adicionar uma falta.

O que eu descobri nesses dois dias foi a utilizar Spinners, ListViews e charts. Como não são mudanças muito significativas (apenas manipulação de layouts) não tem muito o que escrever.

O próximo passo é mudar o design da Home. Planejo colocar um outro piechart, junto a uma série banners ordenados do estado mais emergencial de faltas ao mais tranquilo. A ideia é entrar na Home e já entender sua situação sobre o controle de faltas. Mas, para isso, preciso descobrir como trabalhar com percentuais, pois está acontecendo um erro ao escrever as porcentagens nos logs.