/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mephi.b22901.l3333;

/**
 *
 * @author ivis2
 */

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.swing.filechooser.FileNameExtensionFilter;


public class MonsterApp extends JFrame {
    private JTree monsterTree;
    private JPanel detailPanel;
    private JTextField editField;
    private JButton saveButton;
    private JButton exportButton;
    private List<MonsterStorage> storages;
    private Monster selectedMonster;
    private Chain chain;

    public MonsterApp() {
        super("Bestiary Application");
        storages = new ArrayList<>();
        chain = new Chain();

        setupUI();
        setupListeners();

        SwingUtilities.invokeLater(() -> {
            loadFilesFromProjectDir();
        });
    }

    private void loadFilesFromProjectDir() {
        File projectDir = new File(System.getProperty("user.dir"));

        FileFilter monsterFilesFilter = file -> {
            String name = file.getName().toLowerCase();
            return (name.endsWith(".json") || 
                    name.endsWith(".xml") || 
                    name.endsWith(".yaml") || 
                    name.endsWith(".yml")) &&
                   !name.equals("pom.xml");
        };

        File[] files = projectDir.listFiles(monsterFilesFilter);

        if (files != null && files.length > 0) {
            int result = JOptionPane.showConfirmDialog(this, 
                "Найдены файлы монстров в директории проекта. Загрузить их (" + files.length + " файлов)?",
                "Загрузка файлов", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

            if (result == JOptionPane.YES_OPTION) {
                for (File file : files) {
                    String fileName = file.getAbsolutePath();
                    String fileType = fileName.substring(fileName.lastIndexOf('.') + 1).toUpperCase();
                    MonsterStorage storage = new MonsterStorage(fileType);

                    if (chain.process(fileName, storage)) {
                        storages.add(storage);
                    }
                }

                updateTree();
                exportButton.setEnabled(!storages.isEmpty());

                JOptionPane.showMessageDialog(this,
                    "Успешно загружено " + storages.stream().mapToInt(s -> s.getMonsters().size()).sum() + " монстров",
                    "Загрузка завершена", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void loadFiles() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON, XML, YAML files", 
            "json", "xml", "yaml", "yml"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JSON files", "json"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("XML files", "xml"));
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("YAML files", "yaml", "yml"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File[] files = fileChooser.getSelectedFiles();

            for (File file : files) {
                String fileName = file.getAbsolutePath();
                String fileType = fileName.substring(fileName.lastIndexOf('.') + 1).toUpperCase();
                MonsterStorage storage = new MonsterStorage(fileType);

                if (chain.process(fileName, storage)) {
                    storages.add(storage);
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Не удалось обработать файл: " + file.getName() + 
                        "\nПоддерживаемые форматы: JSON, XML, YAML", 
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }

            updateTree();
            exportButton.setEnabled(!storages.isEmpty());
        }
    }
    
    private void setupUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        JPanel treePanel = new JPanel(new BorderLayout());
        treePanel.setBackground(new Color(211, 211, 211));
        monsterTree = new JTree(new DefaultMutableTreeNode("Чудовища"));
        JScrollPane treeScroll = new JScrollPane(monsterTree);
        treePanel.add(treeScroll, BorderLayout.CENTER);
        
        JButton loadButton = new JButton("Загрузить файлы");
        loadButton.addActionListener(e -> loadFiles());
        treePanel.add(loadButton, BorderLayout.SOUTH);
        
        add(treePanel, BorderLayout.WEST);

        detailPanel = new JPanel(new BorderLayout());

        JPanel infoPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        JScrollPane infoScroll = new JScrollPane(infoPanel);
        detailPanel.add(infoScroll, BorderLayout.CENTER);

        JPanel editPanel = new JPanel(new BorderLayout());

        editPanel.add(new JLabel("Редактировать описание:"), BorderLayout.NORTH);
        editField = new JTextField();
        editField.setBackground(new Color(255, 245, 238)); // Светло-розовый фон для поля редактирования
        editField.setForeground(Color.RED);
        editPanel.add(editField, BorderLayout.CENTER);

        saveButton = new JButton("Сохранить");
        saveButton.setBackground(new Color(144, 238, 144));
        saveButton.setEnabled(false);
        editPanel.add(saveButton, BorderLayout.SOUTH);

        detailPanel.add(editPanel, BorderLayout.SOUTH);

        exportButton = new JButton("Экспортировать все");
        exportButton.setEnabled(false);
        detailPanel.add(exportButton, BorderLayout.NORTH);

        add(detailPanel, BorderLayout.CENTER);
    }

    private void setupListeners() {
        exportButton.addActionListener(e -> exportData());
        monsterTree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) monsterTree.getLastSelectedPathComponent();

//            exportButton.setEnabled(node != null && !node.isRoot() && 
//                node.getParent() != null && node.getParent().equals(monsterTree.getModel().getRoot()));

            if (node == null || node.isRoot()) {
                clearDetailPanel();
                exportButton.setEnabled(false);
                return;
            }

            if (node.getParent() != null && node.getParent().equals(monsterTree.getModel().getRoot())) {
                clearDetailPanel();
                exportButton.setEnabled(true);
                return;
            }
            
            exportButton.setEnabled(false);

            String monsterName = node.toString();
            DefaultMutableTreeNode storageNode = (DefaultMutableTreeNode) node.getParent();
            String storageType = storageNode.toString();
        
            for (MonsterStorage storage : storages) {
                if (storage.getSourceType().equals(storageType)) {
                    for (Monster monster : storage.getMonsters()) {
                        if (monster.getName().equals(monsterName)) {
                            selectedMonster = monster;
                            break;
                        }
                    }
                    if (selectedMonster != null) break;
                }
            }
        
            if (selectedMonster != null) {
                updateDetailPanel(selectedMonster);
            }
        });

        saveButton.addActionListener(e -> {
            if (selectedMonster != null) {
                selectedMonster.setDescription(editField.getText());
                JOptionPane.showMessageDialog(this, "Описание сохранено для " + selectedMonster.getName() + 
                    " в формате " + getCurrentStorageType());
                updateTree();
            }
        });

    }

    private String getCurrentStorageType() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) monsterTree.getLastSelectedPathComponent();
        if (node == null || node.getParent() == null) return "";
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
        if (parent.equals(monsterTree.getModel().getRoot())) return parent.toString();
        return "";
    }
    
    private void updateTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Чудовища");

        for (MonsterStorage storage : storages) {
            DefaultMutableTreeNode storageNode = new DefaultMutableTreeNode(storage.getSourceType());

            for (Monster monster : storage.getMonsters()) {
                DefaultMutableTreeNode monsterNode = new DefaultMutableTreeNode(monster.getName());
                storageNode.add(monsterNode);
            }

            root.add(storageNode);
        }

        monsterTree.setModel(new DefaultTreeModel(root));

        for (int i = 0; i < monsterTree.getRowCount(); i++) {
            monsterTree.expandRow(i);
        }
    }
    
    private void exportData() {
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) monsterTree.getLastSelectedPathComponent();

        if (selectedNode == null || selectedNode.isRoot() || 
            (selectedNode.getParent() != null && !selectedNode.getParent().equals(monsterTree.getModel().getRoot()))) {
            JOptionPane.showMessageDialog(this, 
                "Пожалуйста, выберите формат (JSON, XML или YAML) для экспорта", 
                "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String storageType = selectedNode.toString();
        MonsterStorage selectedStorage = null;

        for (MonsterStorage storage : storages) {
            if (storage.getSourceType().equals(storageType)) {
                selectedStorage = storage;
                break;
            }
        }

        if (selectedStorage == null || selectedStorage.getMonsters().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Нет данных для экспорта в выбранном формате", 
                "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        fileChooser.setDialogTitle("Экспорт " + storageType);

        String defaultExtension = storageType.toLowerCase();
        fileChooser.setFileFilter(new FileNameExtensionFilter(
            defaultExtension.toUpperCase() + " files", defaultExtension));

        if (!defaultExtension.equals("json")) {
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("JSON files", "json"));
        }
        if (!defaultExtension.equals("xml")) {
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("XML files", "xml"));
        }
        if (!defaultExtension.equals("yaml") && !defaultExtension.equals("yml")) {
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("YAML files", "yaml", "yml"));
        }

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String fileName = file.getAbsolutePath();
            String extension = ((FileNameExtensionFilter) fileChooser.getFileFilter()).getExtensions()[0];

            if (!fileName.toLowerCase().endsWith("." + extension)) {
                fileName += "." + extension;
                file = new File(fileName);
            }

            if (chain.processExport(fileName, selectedStorage.getMonsters())) {
                JOptionPane.showMessageDialog(this, 
                    "Данные успешно экспортированы в " + fileName, 
                    "Экспорт завершен", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Не удалось экспортировать файл", 
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void clearDetailPanel() {
        JPanel infoPanel = (JPanel) ((JScrollPane) detailPanel.getComponent(0)).getViewport().getView();
        infoPanel.removeAll();
        infoPanel.revalidate();
        infoPanel.repaint();
        editField.setText("");
        saveButton.setEnabled(false);
        selectedMonster = null;
    }

    private void updateDetailPanel(Monster monster) {
        JPanel infoPanel = (JPanel) ((JScrollPane) detailPanel.getComponent(0)).getViewport().getView();
        infoPanel.removeAll();

        infoPanel.add(new JLabel("Формат:"));
        infoPanel.add(new JLabel(getCurrentStorageType()));

        infoPanel.add(new JLabel("Имя:"));
        infoPanel.add(new JLabel(monster.getName()));


        infoPanel.add(new JLabel("Описание:"));
        editField.setText(monster.getDescription());
        infoPanel.add(new JLabel(monster.getDescription()));

        infoPanel.add(new JLabel("Уровень опасности:"));
        infoPanel.add(new JLabel(String.valueOf(monster.getDangerLevel())));

        infoPanel.add(new JLabel("Место обитания:"));
        infoPanel.add(new JLabel(monster.getHabitat()));

        infoPanel.add(new JLabel("Первое упоминание:"));
        infoPanel.add(new JLabel(monster.getFirstMention()));

        infoPanel.add(new JLabel("Иммунитеты:"));
        infoPanel.add(new JLabel(String.join(", ", monster.getImmunities())));

        infoPanel.add(new JLabel("Уязвимость:"));
        infoPanel.add(new JLabel(monster.getVulnerability() != null ? monster.getVulnerability() : "нет"));

        infoPanel.add(new JLabel("Время активности:"));
        infoPanel.add(new JLabel(monster.getActivityTime()));

        infoPanel.add(new JLabel("Размер:"));
        infoPanel.add(new JLabel(monster.getSize()));

        infoPanel.add(new JLabel("Вес:"));
        infoPanel.add(new JLabel(monster.getWeight()));

        infoPanel.add(new JLabel("Источник:"));
        infoPanel.add(new JLabel(monster.getSource()));

        Map<String, Object> recipe = monster.getRecipe();
        infoPanel.add(new JLabel("Ингредиенты:"));
        infoPanel.add(new JLabel(recipe.get("ingredients").toString()));

        infoPanel.add(new JLabel("Время приготовления:"));
        infoPanel.add(new JLabel(recipe.get("timeMinutes") + " мин"));

        infoPanel.add(new JLabel("Эффективность:"));
        infoPanel.add(new JLabel(recipe.get("effectiveness").toString()));

        infoPanel.revalidate();
        infoPanel.repaint();
        saveButton.setEnabled(true);
    }
}

