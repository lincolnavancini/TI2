package Principal;

import DAO.DAO;
import DAO.Usuario;
import java.util.List;
import java.util.Scanner;

public class Principal {
    private static DAO usuarioDAO = new DAO();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int opcao;
        
        do {
            exibirMenu();
            opcao = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcao) {
                case 1:
                    listar();
                    break;
                case 2:
                    inserir();
                    break;
                case 3:
                    excluir();
                    break;
                case 4:
                    atualizar();
                    break;
                case 5:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
            
            if (opcao != 5) {
                System.out.println("\nPressione Enter para continuar...");
                scanner.nextLine();
            }
            
        } while (opcao != 5);
        
        usuarioDAO.fecharConexao();
        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("\n=== MENU CRUD ===");
        System.out.println("1. Listar registros");
        System.out.println("2. Inserir registro");
        System.out.println("3. Excluir registro");
        System.out.println("4. Atualizar registro");
        System.out.println("5. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static void listar() {
        System.out.println("\n=== LISTAR REGISTROS ===");
        List<Usuario> registros = usuarioDAO.listarTodos();
        
        if (registros.isEmpty()) {
            System.out.println("Nenhum registro encontrado.");
        } else {
            for (Usuario usuario : registros) {
                System.out.println(usuario);
            }
        }
    }

    private static void inserir() {
        System.out.println("\n=== INSERIR REGISTRO ===");
        
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Idade: ");
        int idade = scanner.nextInt();
        scanner.nextLine();
        
        Usuario usuario = new Usuario(nome, email, idade);
        
        int idGerado = usuarioDAO.inserir(usuario);
        if (idGerado != -1) {
            System.out.println("Registro inserido com sucesso! ID: " + idGerado);
        } else {
            System.out.println("Erro ao inserir registro.");
        }
    }

    private static void excluir() {
        System.out.println("\n=== EXCLUIR REGISTRO ===");
        
        System.out.print("ID do registro a excluir: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario == null) {
            System.out.println("Registro não encontrado!");
            return;
        }
        
        System.out.println("Registro encontrado: " + usuario);
        System.out.print("Confirmar exclusão? (s/n): ");
        String confirmacao = scanner.nextLine();
        
        if (confirmacao.equalsIgnoreCase("s")) {
            if (usuarioDAO.excluir(id)) {
                System.out.println("Registro excluído com sucesso!");
            } else {
                System.out.println("Erro ao excluir registro.");
            }
        } else {
            System.out.println("Exclusão cancelada.");
        }
    }

    private static void atualizar() {
        System.out.println("\n=== ATUALIZAR REGISTRO ===");
        
        System.out.print("ID do registro a atualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario == null) {
            System.out.println("Registro não encontrado!");
            return;
        }
        
        System.out.println("Registro atual: " + usuario);
        System.out.println("\nDigite os novos dados (deixe em branco para manter o valor atual):");
        
        System.out.print("Novo nome [" + usuario.getNome() + "]: ");
        String nome = scanner.nextLine();
        if (!nome.isEmpty()) {
            usuario.setNome(nome);
        }
        
        System.out.print("Novo email [" + usuario.getEmail() + "]: ");
        String email = scanner.nextLine();
        if (!email.isEmpty()) {
            usuario.setEmail(email);
        }
        
        System.out.print("Nova idade [" + usuario.getIdade() + "]: ");
        String idadeStr = scanner.nextLine();
        if (!idadeStr.isEmpty()) {
            usuario.setIdade(Integer.parseInt(idadeStr));
        }
        
        if (usuarioDAO.atualizar(usuario)) {
            System.out.println("Registro atualizado com sucesso!");
        } else {
            System.out.println("Erro ao atualizar registro.");
        }
    }
}