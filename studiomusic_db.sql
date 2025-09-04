-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Tempo de geração: 04/07/2025 às 22:24
-- Versão do servidor: 10.4.32-MariaDB
-- Versão do PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Banco de dados: `studiomusic_db`
--

-- --------------------------------------------------------

--
-- Estrutura para tabela `agendamentos`
--

CREATE TABLE `agendamentos` (
  `id_agendamento` int(11) NOT NULL,
  `id_cliente` int(11) NOT NULL,
  `id_sala` int(11) NOT NULL,
  `data_agendamento` date NOT NULL,
  `hora_inicio` time NOT NULL,
  `hora_fim` time NOT NULL,
  `status` varchar(50) NOT NULL,
  `valor_total` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `clientes`
--

CREATE TABLE `clientes` (
  `id_cliente` int(11) NOT NULL,
  `nome` varchar(100) NOT NULL,
  `cpf_cnpj` varchar(20) DEFAULT NULL,
  `telefone` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `ativo` tinyint(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `clientes`
--

INSERT INTO `clientes` (`id_cliente`, `nome`, `cpf_cnpj`, `telefone`, `email`, `ativo`) VALUES
(1, 'clienteste', '38130655888', '11992923881', 'clienteste@email.com', 0),
(2, 'clienteste', '38130655881', '11992923881', 'clienteste@email.com', 0),
(3, 'giullia', '46578698789', '11986578744', 'giullia@email.com', 0),
(4, 'teste', '4567890789', '11976543245678', 'teste@gmail.com', 1),
(5, 'oiteste', '456789098765', '11945678987654', 'oiteste@gmail.com', 1);

-- --------------------------------------------------------

--
-- Estrutura para tabela `salas`
--

CREATE TABLE `salas` (
  `id_sala` int(11) NOT NULL,
  `nome_tipo` varchar(100) NOT NULL,
  `numero_sala` int(11) NOT NULL,
  `capacidade` int(11) DEFAULT NULL,
  `equipamentos` text DEFAULT NULL,
  `valor_hora` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `salas`
--

INSERT INTO `salas` (`id_sala`, `nome_tipo`, `numero_sala`, `capacidade`, `equipamentos`, `valor_hora`) VALUES
(1, 'Sala A - Gravação', 1, 5, 'Microfones Condensadores, Interface de Áudio, Monitores de Referência', 100.00),
(2, 'Sala A - Gravação', 2, 5, 'Microfones Condensadores, Interface de Áudio, Monitores de Referência', 100.00),
(3, 'Sala A - Gravação', 3, 5, 'Microfones Condensadores, Interface de Áudio, Monitores de Referência', 100.00),
(4, 'Sala B - Ensaio', 1, 10, 'Bateria, Amplificadores de Guitarra/Baixo, PA', 75.00),
(5, 'Sala B - Ensaio', 2, 10, 'Bateria, Amplificadores de Guitarra/Baixo, PA', 75.00),
(6, 'Sala B - Ensaio', 3, 10, 'Bateria, Amplificadores de Guitarra/Baixo, PA', 75.00),
(7, 'Sala C - Mixagem', 1, 3, 'Controlador MIDI, Monitores de Estúdio, Software DAW', 120.00),
(8, 'Sala C - Mixagem', 2, 3, 'Controlador MIDI, Monitores de Estúdio, Software DAW', 120.00),
(9, 'Sala C - Mixagem', 3, 3, 'Controlador MIDI, Monitores de Estúdio, Software DAW', 120.00);

-- --------------------------------------------------------

--
-- Estrutura para tabela `usuarios`
--

CREATE TABLE `usuarios` (
  `id_usuario` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `tipo_usuario` varchar(20) NOT NULL,
  `id_cliente_fk` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `usuarios`
--

INSERT INTO `usuarios` (`id_usuario`, `username`, `password`, `tipo_usuario`, `id_cliente_fk`) VALUES
(1, 'admin', 'admin', 'admin', NULL),
(2, 'giu', 'giu123', 'cliente', 3),
(3, 'teste', '1234', 'cliente', 4),
(4, 'oi', 'oi123', 'cliente', 5);

--
-- Índices para tabelas despejadas
--

--
-- Índices de tabela `agendamentos`
--
ALTER TABLE `agendamentos`
  ADD PRIMARY KEY (`id_agendamento`),
  ADD UNIQUE KEY `unique_agendamento` (`id_sala`,`data_agendamento`,`hora_inicio`),
  ADD KEY `id_cliente` (`id_cliente`);

--
-- Índices de tabela `clientes`
--
ALTER TABLE `clientes`
  ADD PRIMARY KEY (`id_cliente`),
  ADD UNIQUE KEY `cpf_cnpj` (`cpf_cnpj`);

--
-- Índices de tabela `salas`
--
ALTER TABLE `salas`
  ADD PRIMARY KEY (`id_sala`),
  ADD UNIQUE KEY `unique_sala_type_number` (`nome_tipo`,`numero_sala`);

--
-- Índices de tabela `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id_usuario`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `id_cliente_fk` (`id_cliente_fk`);

--
-- AUTO_INCREMENT para tabelas despejadas
--

--
-- AUTO_INCREMENT de tabela `agendamentos`
--
ALTER TABLE `agendamentos`
  MODIFY `id_agendamento` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de tabela `clientes`
--
ALTER TABLE `clientes`
  MODIFY `id_cliente` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de tabela `salas`
--
ALTER TABLE `salas`
  MODIFY `id_sala` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT de tabela `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id_usuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Restrições para tabelas despejadas
--

--
-- Restrições para tabelas `agendamentos`
--
ALTER TABLE `agendamentos`
  ADD CONSTRAINT `agendamentos_ibfk_1` FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id_cliente`) ON DELETE CASCADE,
  ADD CONSTRAINT `agendamentos_ibfk_2` FOREIGN KEY (`id_sala`) REFERENCES `salas` (`id_sala`) ON DELETE CASCADE;

--
-- Restrições para tabelas `usuarios`
--
ALTER TABLE `usuarios`
  ADD CONSTRAINT `usuarios_ibfk_1` FOREIGN KEY (`id_cliente_fk`) REFERENCES `clientes` (`id_cliente`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
