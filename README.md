# StudioMusic
O StudioMusic é um sistema desktop completo para a gestão de agendamentos em estúdios de música. Desenvolvido em Java, o projeto foi concebido para automatizar a coordenação de salas, clientes e reservas, resolvendo problemas comuns como conflitos de horário e falta de um registro centralizado.
A aplicação se destaca pela sua arquitetura robusta e pela implementação de funcionalidades essenciais para um ambiente de negócios real, como o gerenciamento seguro de acessos e a persistência de dados confiável.

Principais Funcionalidades
Autenticação e Perfis de Usuário: Sistema de login com dois perfis distintos: Administrador e Cliente.

Gerenciamento de Clientes (CRUD): O administrador pode cadastrar, consultar, atualizar e desativar clientes de forma lógica, preservando o histórico de dados.

Gestão de Agendamentos: Clientes e administradores podem criar, visualizar, atualizar e cancelar agendamentos. O sistema verifica a disponibilidade de múltiplas salas do mesmo tipo em tempo real para evitar conflitos.

Segurança de Senha: O administrador pode redefinir senhas de clientes, enquanto o cliente pode atualizar sua própria senha, garantindo a segurança das credenciais.

Auto-cadastro: Novos clientes podem se cadastrar diretamente pela tela de login para maior autonomia.

Arquitetura e Estrutura do Código
O projeto segue a arquitetura Model-View-Controller (MVC), com uma separação clara de responsabilidades em pacotes dedicados:

model/: Contém as classes de entidades (Cliente, Sala, Agendamento, Usuario).

dao/: Camada de Data Access Object, responsável pela comunicação direta com o banco de dados MySQL via JDBC.

controller/: O "cérebro" da aplicação. Contém a lógica de negócio, validações e orquestra as interações entre a View e o Model.

view/: A camada de interface do usuário, construída com Java Swing, com telas otimizadas e um design limpo e moderno.

Tecnologias Utilizadas
Linguagem de Programação: Java

Interface Gráfica: Java Swing

Banco de Dados: MySQL

Conexão com o BD: JDBC (Java Database Connectivity)

