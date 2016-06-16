#!/bin/bash
echo "Deletando a base atual"
sudo -u postgres dropdb leilao
sudo -u postgres dropdb leilao_test
echo "Criando usuário"
sudo -u postgres psql -c "CREATE USER leilao WITH PASSWORD 'leiloeiro'"
echo "Criando nova base de dados"
sudo -u postgres createdb leilao -O leilao
sudo -u postgres createdb leilao_test -O leilao
sudo -u postgres psql leilao < dump.sql
#sudo -u postgres psql -h localhost -d leilao -c "insert into usuario (type, apelido, nome, saldo, senha) values ('1','Administrador', 'admin',100,'admin')"
#sudo -u postgres psql leilao_test < src/test/resources/pgTest.sql
