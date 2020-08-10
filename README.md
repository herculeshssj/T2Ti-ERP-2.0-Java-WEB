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

Use o arquivo __initialScript.sql__ que est� na pasta infra/Scripts para criar o usu�rio e a base de dados.

Ap�s criado a base de dados, restaure o backup que est� na pasta infra:

```
docker exec -i t2tierp-postgresql psq -U t2tierp < script_bd_infra_controle_acesso.backup
```

Ap�s restaurar o backup, altere a senha do usu�rio admin para '123456':

```
docker exec -i t2tierp-postgresql psql -U t2tierp -d t2tierp -c "update usuario set senha = 'e10adc3949ba59abbe56e057f20f883e' where id = 1"
```