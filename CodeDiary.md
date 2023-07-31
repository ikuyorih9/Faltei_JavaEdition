# Diário de programação!

**Por: Hugo Hiroyuki Nakamura.**

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

## 31 de julho de 2023: tentativa de criar uma Lateral Toolbar.

Hoje eu tentei criar uma barra de ferramentas lateral. Eu copiei um outro exemplo, onde ele usa **navigators** e **fragments**. Os fragmentos são fáceis de entender, visto que eles são literalmente fragmentos da tela, onde você junta num layout só. Os navegadores, eu copiei o código, esperando entender. 

O problema da vez foi que, copiando o código do exemplo, o meu layout não disponibiliza o botão de abrir a barra lateral, ele troca pelo botão de voltar (navigate up). Eu entendi que o código não consegue entender que existe um top-level destionation. Não sei como resolver e vou deixar isso pra outro dia.

<p align="center">
    <img width=180 src="diaryImages/31.07.23 - problema na navegacao.png">
</p>