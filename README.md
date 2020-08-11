Projeto T2Ti ERP 2.0
====================

![](http://t2ti.com/images/erp/logo_erp_2.png)

Aqui � poss�vel baixar o c�digo fonte do Projeto T2Ti ERP 2.0 em Java WEB. Este treinamento formou milhares de desenvolvedores no Brasil e ajudou centenas deles a lan�ar seu pr�prio ERP no mercado. Mais informa��es sobre o treinamento podem ser encontradas no seguinte link: http://t2ti.com/erp2/.


## T2Ti ERP 2.0 - Escopo

![](http://t2ti.com/images/erp/t2ti_erp_2.jpg)

### Bloco Administrativo

**O Bloco Administrativo � formado por cinco m�dulos:**

- **Cadastros Base:** engloba os cadastros que s�o usados por diversos m�dulos do ERP.
- **[GED - Gest�o Eletr�nica de Documentos](http://t2ti.com/erp2/artigos/GED.pdf)**
- **[Gerador de Etiquetas](http://t2ti.com/erp2/artigos/GeradorEtiquetas.pdf)**
- **[Agenda Corporativa](http://t2ti.com/erp2/artigos/AgendaCorporativa.pdf)**
- **[BI - Business Intelligence](http://t2ti.com/erp2/artigos/BI.pdf)**

## Forma de Desenvolvimento Java Web

![](http://t2ti.com/images/erp/jsf-arch.jpg)

O ERP Java Web � feito com JSF como tecnologia base e frameworks auxiliares que s�o apresentados aos Participantes durante o treinamento. Como uma aplica��o Web, tudo o que o usu�rio vai precisar � de um browser para usar o ERP.

## Ambiente de desenvolvimento

- Java 11
- Eclipse Java EE
- JBoss Tools
- Wildfly 11
- PostgreSQL 11
- Docker

## Configura��o do ambiente

### PostgreSQL

```
docker volume create t2tierp-psql-data
docker run --restart=unless-stopped --name t2tierp-postgresql -e POSTGRES_PASSWORD=postgres -p 5432:5432 -v t2tierp-psql-data:/var/lib/postgresql/data -d postgres:11
```

Use o arquivo __initialScript.sql__ que est� na pasta infra/t2tierp/Scripts para criar o usu�rio e a base de dados.

Ap�s criado a base de dados, restaure o backup que est� na pasta infra:

```
docker exec -i t2tierp-postgresql psql -U t2tierp < script_bd_infra_controle_acesso.backup
```

Ap�s restaurar o backup, altere a senha do usu�rio admin ('1') para '123456':

```
docker exec -i t2tierp-postgresql psql -U t2tierp -d t2tierp -c "update usuario set senha = 'eeafb716f93fa090d7716749a6eefa72' where id = 1"
```

Logo em seguida, execute a atualiza��o da estrutura da base usando o Flyway configurado no projeto infra:

```
docker run --rm -it --name t2tierp-flyway --net=host -v "$(pwd)":/usr/src/mymaven -w /usr/src/mymaven maven:3-openjdk-11 mvn flyway:baseline
```

```
docker run --rm -it --name t2tierp-flyway --net=host -v "$(pwd)":/usr/src/mymaven -w /usr/src/mymaven maven:3-openjdk-11 mvn flyway:migrate
```

**Obs:** fa�a os ajustes necess�rios no pom.xml referente a conex�o com a base.

### Eclipse

Defina o encoding do workspace para ISO-8859-1.

Instale o JBoss Tools no Eclipse pelo Eclipse Marketplace. Deixe marcado somente a op��o "JBoss AS, WildFly & EAP Server Tools" na janela de sele��o de componentes.

Ap�s a instala��o do JBoss Tools, defina o Wildfly 11 como o servidor principal na view Server, deixando as op��es padr�o marcadas.

Importe os projetos que ser�o trabalhados pela perspectiva Git.

N�o esque�a de realizar um build no projeto atrav�s do Maven -> Update projects e de realizar o mvn install atrav�s do Run As -> Maven install.

Para rodar qualquer um dos projetos, clique direito -> Run As -> Run On Server.

### JBoss

Inicie o Wildfly 11. Entre na pasta bin do Wildfly 11 e execute o script __datasource.cli__. O script est� na pasta infra.

```
./jboss-cli.sh --file=datasource.cli 
```

## Realizando o deploy

Cada projeto produz seu pr�prio arquivo .war, que pode ser adicionado na mesma inst�ncia ou em outras inst�ncias de Wildfly.

Para gerar o arquivo .war, clique direito no projeto -> Run As -> Maven Install...

O arquivo .war estar� na pasta target/.

Crie uma pasta chamada deployments no diret�rio home.

Pare qualquer inst�ncia do Wildfly que est� atualmente em execu��o.

Suba o container do Wildfly 11:

```
docker run --name t2tierp-wildfly --net=host -v "$(pwd)"/deployments:/opt/jboss/wildfly/standalone/deployments -d jboss/wildfly:11.0.0.Final
```

Copie o arquivo datasource.cli e o driver JDBC para dentro do container:

```
docker cp datasource.cli t2tierp-wildfly:/opt/jboss
docker cp postgresql-42.2.14.jar t2tierp-wildfly:/opt/jboss
```

Entre dentro do container:

```
docker exec -it t2tierp-wildfly bash
```

Execute o script:

```
./wildfly/bin/jboss-cli.sh --file=datasource.cli
```

Saia do container e copie o arquivo WAR para a pasta deployments e aguarde finalizar o deploy.