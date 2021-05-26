package br.com.fulltime.service;

import br.com.fulltime.modelo.Aluno;
import br.com.fulltime.modelo.Arquivo;
import br.com.fulltime.modelo.Mensagem;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ManuseamentoArquivo {

    private static String ARQUIVO_TEXTO_TEMPORARIO = "src/main/resources/temp.txt";

    public static boolean imprimirMensagem(Aluno aluno, Mensagem mensagem) throws IOException {
        // Pegar os dados necessários para o cadastro
        try {
            aluno.setNomeAluno(JOptionPane.showInputDialog(null, "Digite o nome do aluno."));
            aluno.setIdentificador(JOptionPane.showInputDialog(null, "Digite o identificador do aluno."));
            mensagem.setDisciplina(JOptionPane.showInputDialog(null, "Digite a disciplina."));
            mensagem.setPrimeiraNota(JOptionPane.showInputDialog(null, "Digite a primeira nota."));
            mensagem.setSegundaNota(JOptionPane.showInputDialog(null, "Digite a segunda nota."));
            mensagem.setAluno(aluno);
        } catch (NullPointerException | StringIndexOutOfBoundsException e) {
            e.printStackTrace();
            return false;
        }

        // Construir a nova linha
        var stringBuilder = new StringBuilder();
        stringBuilder.append('@');
        stringBuilder.append(String.format("%06d", mensagem.getIdentificador()));
        stringBuilder.append(';');
        stringBuilder.append(mensagem.getNomeAluno());
        stringBuilder.append(';');
        stringBuilder.append(mensagem.getDisciplina());
        stringBuilder.append(";N1");
        var n1 = (int) (mensagem.getPrimeiraNota() * 10000);
        stringBuilder.append(String.format("%04d", n1));
        stringBuilder.append(";N2");
        var n2 = (int) (mensagem.getSegundaNota() * 10000);
        stringBuilder.append(String.format("%04d", n2));
        stringBuilder.append('|');
        stringBuilder.append('\n');

        // Anexar a nova linha ao arquivo de texto

        var bufferedWriter = new BufferedWriter(new FileWriter(Arquivo.ARQUIVO_NOTAS, true));
        bufferedWriter.append(stringBuilder);
        bufferedWriter.close();

        return true;

    }

    public static void limparLinha(long identificador, String disciplina) throws IOException {

        {
            // Ler linha por linha o conteúdo do arquivo de notas e passando para um arquivo temporário,
            // pulando o arquivo que contém o identificador provido

            var scanner = new Scanner(new File(Arquivo.ARQUIVO_NOTAS));
            BufferedWriter buffWriter = new BufferedWriter(new FileWriter(ARQUIVO_TEXTO_TEMPORARIO));
            String linha;

            do {
                linha = scanner.nextLine();
                if (linha.contains((identificador + "")) && linha.contains(disciplina)) {
                    continue;
                } else {
                    buffWriter.write(linha + "\n");
                }
            } while (scanner.hasNext());

            buffWriter.flush();
            buffWriter.close();
            scanner.close();
        }
        {
            // Transferir o conteúdo do arquivo temporário para o principal

            Scanner scanner = new Scanner(new File(ARQUIVO_TEXTO_TEMPORARIO));
            BufferedWriter buffWriter = new BufferedWriter(new FileWriter(Arquivo.ARQUIVO_NOTAS));
            String valor;

            do {
                valor = scanner.nextLine();
                buffWriter.write(valor + "\n");
            } while (scanner.hasNext());

            buffWriter.flush();
            File temp = new File(ARQUIVO_TEXTO_TEMPORARIO);
            temp.delete();
            buffWriter.close();
            scanner.close();

            JOptionPane.showMessageDialog(null, String.format("Nota com o ID %06d foi excluída com sucesso.", identificador));
        }
    }

}
