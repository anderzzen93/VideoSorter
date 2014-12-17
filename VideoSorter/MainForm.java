package VideoSorter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MainForm extends JFrame{

	private static final long serialVersionUID = 1L;

	private JTable mainTable;
	private JScrollPane mainTableScroll;
	private DefaultTableModel mainTableModel;
	private HashMap<String, JLabel> labels;
	private JTextField titelSearch;
	private JTextField genreSearch;
	private JTextField directorSearch;
	private JTextField yearSearch;
	private JTextField ratingSearch;
	private JComboBox<Character> lengthOperator;
	private JTextField lengthSearch;
	private JTextField stringSearch;
	private JButton searchButton;
	private JButton stringSearchButton;

	private FileManager fileManager;



	public MainForm(String path){

		fileManager = new FileManager(path);
		fileManager.loadVideos();

		setTitle("Projekt");
		setLayout(null);
		setSize(1200, 768);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		initUI();
	}


	private void updateList(String titel, String genre){
		while(mainTableModel.getRowCount() > 0)
			mainTableModel.removeRow(0);

		for (Video v : fileManager.getVideos()){
			if ((titel == "" || v.getMetaData().getName().toLowerCase().contains(titel.toLowerCase())) && (genre == "" || v.getMetaData().getGenre().toLowerCase().contains(genre.toLowerCase()))){
				mainTableModel.addRow(new Object[]{v.getMetaData().getName(), v.getMetaData().getGenre()});
			}
		}
	}

	private void initUI(){

		labels = new HashMap<String, JLabel>();
		mainTableModel = new DefaultTableModel(0,0);
		mainTable = new JTable();
		mainTableScroll = new JScrollPane(mainTable);
		titelSearch = new JTextField();
		genreSearch = new JTextField();
		directorSearch = new JTextField();
		yearSearch = new JTextField();
		ratingSearch = new JTextField();
		lengthOperator = new JComboBox<Character>();
		lengthSearch = new JTextField();

		searchButton = new JButton();
		stringSearchButton = new JButton();
		stringSearch = new JTextField();


		labels.put("titel_label", new JLabel());
		labels.put("genre_label", new JLabel());
		labels.put("director_label", new JLabel());
		labels.put("year_label", new JLabel());
		labels.put("rating_label", new JLabel());
		labels.put("length_label", new JLabel());
		labels.put("string_label", new JLabel());

		mainTableModel.setColumnIdentifiers(new String[]{"Titel", "Genre", "Regissör", "År", "Betyg 1-5", "Längd"});
		mainTable.setModel(mainTableModel);
		mainTableScroll.setSize(800, 690);
		mainTableScroll.setLocation(20, 30);

		labels.get("titel_label").setText("Titel:");
		labels.get("titel_label").setSize(70, 20);
		labels.get("titel_label").setLocation(840, 30);

		labels.get("genre_label").setText("Genre:");
		labels.get("genre_label").setSize(70, 20);
		labels.get("genre_label").setLocation(840, 60);

		labels.get("director_label").setText("Regissör:");
		labels.get("director_label").setSize(70, 20);
		labels.get("director_label").setLocation(840, 90);

		labels.get("year_label").setText("År:");
		labels.get("year_label").setSize(70, 20);
		labels.get("year_label").setLocation(840, 120);

		labels.get("rating_label").setText("Betyg: 1-5");
		labels.get("rating_label").setSize(80, 20);
		labels.get("rating_label").setLocation(840, 150);

		labels.get("length_label").setText("Längd:");
		labels.get("length_label").setSize(70, 20);
		labels.get("length_label").setLocation(840, 180);

		labels.get("string_label").setText("Söksträng:");
		labels.get("string_label").setSize(70,20);
		labels.get("string_label").setLocation(840, 270);

		titelSearch.setSize(200, 20);
		titelSearch.setLocation(920, 31);

		genreSearch.setSize(200, 20);
		genreSearch.setLocation(920, 61);

		directorSearch.setSize(200, 20);
		directorSearch.setLocation(920, 91);

		yearSearch.setSize(200, 20);
		yearSearch.setLocation(920, 121);

		ratingSearch.setSize(200, 20);
		ratingSearch.setLocation(920, 151);

		lengthOperator.addItem('<');
		lengthOperator.addItem('>');
		lengthOperator.setSize(40, 20);
		lengthOperator.setLocation(1080, 181);

		lengthSearch.setText("hh:mm");
		lengthSearch.setSize(155, 20);
		lengthSearch.setLocation(920, 181);

		stringSearch.setSize(280,20);
		stringSearch.setLocation(840, 300);

		//lengthOperator.setSize()

		searchButton.setSize(100, 30);
		searchButton.setLocation(1020, 210);
		searchButton.setText("Sök");
		searchButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				updateList(titelSearch.getText(), genreSearch.getText());
			}
		});

		stringSearchButton.setSize(100, 30);
		stringSearchButton.setLocation(1020, 330);
		stringSearchButton.setText("Sök");
		stringSearchButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// todo 
				while(mainTableModel.getRowCount() > 0)
					mainTableModel.removeRow(0);
				for (Video v : new SearchStringInterpreter().interpretString(stringSearch.getText(), fileManager.getVideos()))
					mainTableModel.addRow(new Object[]{v.getMetaData().getName(), v.getMetaData().getGenre()});
			}
		});


		for (Video v : fileManager.getVideos()){
			mainTableModel.addRow(new Object[]{v.getMetaData().getName(), v.getMetaData().getGenre()});
		}

		mainTableScroll.setEnabled(true);
		mainTable.setEnabled(true);
		mainTableScroll.setVisible(true);

		add(mainTableScroll);

		for (JLabel l : labels.values()){
			add(l);
		}

		add(titelSearch);
		add(genreSearch);
		add(directorSearch);
		add(yearSearch);
		add(ratingSearch);
		add(searchButton);
		add(lengthSearch);
		add(lengthOperator);
		add(stringSearch);
		add(stringSearchButton);
	}

	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable(){
			@Override
			public void run(){
				System.out.println(System.getProperty("user.home"));
				String path = FileChooser.fileChooser();
				if (!path.equals("")){
					MainForm main = new MainForm(path);
					main.setVisible(true);
				}
			}
		});
	}
}
