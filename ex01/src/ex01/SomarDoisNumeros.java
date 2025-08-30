package ex01;
import java.util.*;

public class SomarDoisNumeros {
	public static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) {
		int n1, n2, soma = 0;
		
		System.out.println("Programa para somar dois numeros inteiros!");
		System.out.println("Digite o primeiro numero: ");
		n1 = sc.nextInt();
		System.out.println("Digite o segundo numero: ");
		n2 = sc.nextInt();
		
		soma = n1 + n2;
		
		System.out.println("A soma dos dois numeros e: " + soma);
	}

}
