package com;

import javax.swing.*;

import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.BreakIterator;
import java.util.*;

public class CDRUpload extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JLabel lbl;
	JLabel lbl1;
	JLabel lbln;
	JLabel lbl2;
	JLabel lbl3;
	JLabel lbl4;
	JLabel lbl5;
	JLabel lbl6;
	JFileChooser fc;
	JButton b, b1;
	JTextField tf;
	FileInputStream in;
	Socket s;
	DataOutputStream dout;
	DataInputStream din;
	String Filename;

	CDRUpload() {

		super("CDR DATA UPLOADER");
		lbl = new JLabel("File To Upload:");
		lbl.setBounds(10, 65, 180, 20);
		add(lbl);
		tf = new JTextField();
		tf.setBounds(95, 60, 200, 30);
		add(tf);

		b = new JButton("Browse");
		b.setBounds(300, 60, 80, 30);
		add(b);
		b.addActionListener(this);
		b1 = new JButton("Upload");
		b1.setBounds(150, 100, 80, 30);
		add(b1);

		lbl1 = new JLabel("");
		lbl1.setBounds(80, 140, 190, 20);
		add(lbl1);
		lbl1.setVisible(false);

		lbln = new JLabel("\n");
		lbln.setBounds(70, 145, 190, 30);
		add(lbln);
		lbln.setVisible(false);

		lbl2 = new JLabel("");
		lbl2.setBounds(70, 140, 260, 50);
		add(lbl2);
		lbl2.setVisible(false);

		lbl3 = new JLabel("");
		lbl3.setBounds(70, 150, 260, 60);
		add(lbl3);
		lbl3.setVisible(false);

		lbl4 = new JLabel("");
		lbl4.setBounds(70, 160, 260, 70);
		add(lbl4);
		lbl4.setVisible(false);

		lbl5 = new JLabel("");
		lbl5.setBounds(70, 170, 280, 80);
		add(lbl5);
		lbl5.setVisible(false);

		lbl6 = new JLabel("");
		lbl6.setBounds(70, 185, 310, 80);
		add(lbl6);
		lbl6.setVisible(false);

		b1.addActionListener(this);
		fc = new JFileChooser();
		setLayout(null);
		setSize(400, 300);
		setVisible(true);

	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		try {
			if (e.getSource() == b) {
				int x = fc.showOpenDialog(null);
				if (x == JFileChooser.APPROVE_OPTION) {
					copy();

				}
			}
			if (e.getSource() == b1) {

				// System.out.println("Naresh:"+Filename);
				File f = new File(Filename);

				// System.out.println(f.exists());

				//FileReader fr = new FileReader(f);

				BufferedReader br = new BufferedReader(new FileReader(f));
				
				File patfile=new File("D:/Research/CID_patterns.txt");
				BufferedReader brpattern = new BufferedReader(new FileReader(patfile));
				
				String str = null;
				String str1 = null;
				String str2 = "";
				String str4 = "";
				int flag = 1;
				int k = 0;
				int l = 0;
				int m = 0;
				int n = 0;
				Class.forName("oracle.jdbc.driver.OracleDriver");

				Connection con = DriverManager.getConnection(
						"jdbc:oracle:thin:@localhost:1521:xe", "system",
						"system");
				Statement stmtt2 = con.createStatement();
				stmtt2.executeUpdate("truncate table CIDRelation");

				Statement stmtt = con.createStatement();
				stmtt.executeUpdate("truncate table SENT_V2");
				Statement stmtt1 = con.createStatement();
				stmtt1.executeUpdate("truncate table SENT_V1");

				Statement stmt1 = con.createStatement();
				stmt1.executeUpdate("delete from SENT");

				Statement stmtadel = con.createStatement();
				stmtadel.executeUpdate("truncate table SENT_no_relation");

				PreparedStatement ps = con
						.prepareStatement("create table dummy(DocumentID number,sentence clob)");
				ps.executeUpdate();

				while ((str = br.readLine()) != null) {
					if (str.contains("|t|")) {
						if (str.contains("|t|")) {
							str1 = str.replace("|t|", " ");
						}
						str4 = "";
						str2 = "";
						String st[] = str1.split(" ");

						if (flag > 2) {
							str2 = "";

							flag = 1;
						}

						for (int i = 0; i < st.length; i++) {

							if (st[i] == " ") {
								break;
							}

							if (i == 0) {
								str4 = str4 + "" + st[i];
							} else if (i == 1) {
								str2 = str2 + "" + st[i];
							} else {
								str2 = str2 + " " + st[i];
							}
							// break;

						}

						// System.out.println(str2);
						// System.out.println(str4);
						String str5[] = str4.split(" ");

						PreparedStatement stmt = con
								.prepareStatement("insert into SENT values(?,?)");
						stmt.setInt(1, Integer.parseInt(str5[0]));
						stmt.setString(2, str2);

						stmt.executeUpdate();

						k++;

					}

					else if (str.contains("CID")) {

						// System.out.println("CID");
						String str8[] = str.split("\t");
						// System.out.println(str8);
						for (int i = 0; i < str8.length; i++) {
							if (i == 1) {

							} else {
								str8[i].trim();
							}
						}
					
						PreparedStatement stmt5 = con
								.prepareStatement("insert into CIDRelation values(?,?,?,?)");
						stmt5.setInt(1, Integer.parseInt(str8[0]));
						stmt5.setString(2, str8[1]);
						stmt5.setString(3, str8[2]);
						stmt5.setString(4, str8[3]);
						stmt5.executeUpdate();

						n++;
					}

					else if (str.contains("Disease") && !(str.contains("|a|"))) {

						// System.out.println("Disease");
						String str6 = str.replace("\t", " ");
						String str7[] = str.split("\t");
						// System.out.println(str7);

						for (int i = 0; i < str7.length; i++) {
							if (i == 1) {

							} else {
								str7[i].trim();
							}
						}

						PreparedStatement stmt3 = con
								.prepareStatement("insert into SENT_V1 values(?,?,?,?,?)");
						stmt3.setInt(1, Integer.parseInt(str7[0]));// 1
																	// specifies
																	// the first
																	// parameter
																	// in the
																	// query
						stmt3.setInt(2, Integer.parseInt(str7[1]));
						stmt3.setInt(3, Integer.parseInt(str7[2]));
						stmt3.setString(4, "<DIS>" + str7[3] + "</DIS>");
						stmt3.setString(5, str7[5]);

						stmt3.executeUpdate();

						l++;

					}

					else if (str.contains("Chemical")) {

						// System.out.println("Chemical");
						String str6 = str.replace("\t", " ");
						String str9[] = str.split("\t");
						// System.out.println(str9);

						for (int i = 0; i < str9.length; i++) {
							if (i == 1) {

							} else {
								str9[i].trim();
							}
						}
						PreparedStatement stmt4 = con
								.prepareStatement("insert into SENT_V2 values(?,?,?,?,?)");
						stmt4.setInt(1, Integer.parseInt(str9[0]));// 1
																	// specifies
						stmt4.setInt(2, Integer.parseInt(str9[1]));
						stmt4.setInt(3, Integer.parseInt(str9[2]));
						stmt4.setString(4, "<CHM>" + str9[3] + "</CHM>");
						stmt4.setString(5, str9[5]);

						stmt4.executeUpdate();

						m++;
					}

					else {

						// String str7[]=str6.split(" ");
						// System.out.println(str7);

					}

					flag++;

				}

				
				//Inserting all the patterns
				String strpattern=null;
				
				while((strpattern=brpattern.readLine())!=null)
				{
					String strpat[]=strpattern.split("\t");
					System.out.println(strpat[1]);
					PreparedStatement stmtpat = con
							.prepareStatement("insert into CID_patterns values(?,?)");
					stmtpat.setString(1, strpat[0]);// 1
																// specifies
					stmtpat.setString(2, strpat[1]);
					

					stmtpat.executeUpdate();
					
				}
				
				
				
				
				
				File f1 = new File(Filename);

				// System.ou5t.println(f1.exists());


				BufferedReader br1 = new BufferedReader(new FileReader(f1));

				String stra = "";
				String stra1 = "";
				String stra2 = "";
				String stra4 = "";
				int p = 0;
				int temp = 1;

				String st[] = null;

				BreakIterator iterator = BreakIterator
						.getSentenceInstance(Locale.US);
				PreparedStatement stmta = con
						.prepareStatement("insert into SENT_no_relation values(?,?,?)");
				while ((stra = br1.readLine()) != null) {
					int sent_numa = 0;
					if (stra.contains("|t|") || stra.contains("|a|")) {
						if (stra.contains("|t|")) {
							stra1 = stra.replace("|t|", " ");
						} else if (stra.contains("|a|")) {
							stra1 = stra.replace("|a|", " ");
						}

						String sta[] = stra1.split(" ");

						if (temp > 2) {
							stra2 = "";
							stra4 = "";
							temp = 1;
						}

						for (int i = 0; i < sta.length; i++) {

							if (sta[i] == " ") {
								break;
							}

							if (i == 0) {
								stra4 = stra4 + " " + sta[i];
							}

							else {

								stra2 = stra2 + " " + sta[i];
							}
							// break;

						}
						if (temp == 2) {
							String disease = stra2.trim();
							// System.out.println(disease);
							// System.out.println(stra4);
							String DocId[] = stra4.split(" ");
							PreparedStatement pst = con
									.prepareStatement("select * from SENT_V1 where DocumentID=? union select * from SENT_V2 where DocumentID=?");

							pst.setInt(1, Integer.parseInt(DocId[1]));
							pst.setInt(2, Integer.parseInt(DocId[1]));
							ResultSet rs = pst.executeQuery();
							int local = 1;

							int position = 0;

							while (rs.next()) {
								if (local == 1) {

								StringBuffer buffer = new StringBuffer(
										disease);

								if (rs.getString(4).contains("<CHM>")) {

									if (position == 0) {
										buffer.replace(rs.getInt(2) + position,rs.getInt(3) + position,"#C ");
										// System.out.println(buffer.toString());
										position=(rs.getInt(3)-rs.getInt(2))-3;
										PreparedStatement ps1 = con
												.prepareStatement("insert into dummy values(?,?)");
										ps1.setInt(1,
												Integer.parseInt(DocId[1]));
										ps1.setString(2, buffer.toString());
										ps1.executeUpdate();
									} else {
										PreparedStatement ps2 = con
												.prepareStatement("select sentence from dummy where DocumentID=?");
										ps2.setInt(1,
												Integer.parseInt(DocId[1]));
										ResultSet rs1 = ps2.executeQuery();
										while (rs1.next()) {
											StringBuffer buff = new StringBuffer(
													rs1.getString(1));
											int newposstart=rs.getInt(2)-position;
											int newposend=rs.getInt(3)-position;
											buff.replace(
													newposstart,
													newposend,
													"#C "
															
															);
											// System.out.println(buff.toString());
											PreparedStatement ps3 = con
													.prepareStatement("update dummy set sentence=? where DocumentID=?");
											ps3.setInt(2, Integer
													.parseInt(DocId[1]));
											ps3.setString(1,
													buff.toString());
											ps3.executeUpdate();
											position=position+((rs.getInt(3)-rs.getInt(2))-3);

										}
									}
								} else if (rs.getString(4)
										.contains("<DIS>")) {

									if (position == 0) {
										buffer.replace(
												rs.getInt(2) + position,
												rs.getInt(3) + position,
												"#D "
														);
										position=(rs.getInt(3)-rs.getInt(2))-3;
										// System.out.println(buffer.toString());
										PreparedStatement ps1 = con
												.prepareStatement("insert into dummy values(?,?)");
										ps1.setInt(1,
												Integer.parseInt(DocId[1]));
										ps1.setString(2, buffer.toString());
										ps1.executeUpdate();
									} else {
										PreparedStatement ps2 = con
												.prepareStatement("select sentence from dummy where DocumentID=?");
										ps2.setInt(1,
												Integer.parseInt(DocId[1]));
										ResultSet rs1 = ps2.executeQuery();
										while (rs1.next()) {
											StringBuffer buff = new StringBuffer(
													rs1.getString(1));
											int newposstart=rs.getInt(2)-position;
											int newposend=rs.getInt(3)-position;
											buff.replace(
													newposstart,
													newposend,
													"#D ");																
											// System.out.println(buff.toString());
											PreparedStatement ps3 = con
													.prepareStatement("update dummy set sentence=? where DocumentID=?");
											ps3.setInt(2, Integer
													.parseInt(DocId[1]));
											ps3.setString(1,
													buff.toString());
											ps3.executeUpdate();
											position=position+((rs.getInt(3)-rs.getInt(2))-3);
										}
									}

								}

								//position = position + 3;

							}
}

							PreparedStatement ps4 = con
									.prepareStatement("select sentence from dummy where DocumentID=?");

							ps4.setInt(1, Integer.parseInt(DocId[1]));

							ResultSet rs3 = ps4.executeQuery();
							while (rs3.next()) {
								iterator.setText(rs3.getString(1));
								int start = iterator.first();
								for (int end = iterator.next(); end != BreakIterator.DONE;

								start = end, end = iterator.next()) {

									String stabs = (rs3.getString(1).substring(
											start, end));
									// System.out.println(stra1.substring(start,end));
									stmta.setInt(1, Integer.parseInt(DocId[1]));

									stmta.setInt(2, sent_numa);
									String stabstract = stabs.trim();
									stmta.setString(3, stabstract);

									stmta.executeUpdate();
									sent_numa++;
									p++;
								}
							}

						}

						temp++;

					}

				}

				System.out.println(k + " records inserted into SENT Table");
				System.out.println(l + " records inserted into SENT_V1 Table");
				System.out.println(m + " records inserted into SENT_V2 Table");
				System.out.println(n
						+ " records inserted into CIDRelation Table");
				System.out.println(p
						+ " records inserted into SENT_no_relation Table");
				lbl1.setText("Data Uploaded Successfully!");
				lbln.setText("\n");
				lbl2.setText("No of rows Inserted into SENT Table:" + k);
				lbl3.setText("No of rows Inserted into SENT_V1 Table:" + l);
				lbl4.setText("No of rows Inserted into SENT_V2 Table:" + m);
				lbl5.setText("No of rows Inserted into CIDRelation Table:" + n);
				lbl6.setText("No of rows Inserted into SENT_no_relation Table:"
						+ p);
				lbl1.setVisible(true);
				lbln.setVisible(true);
				lbl2.setVisible(true);
				lbl3.setVisible(true);
				lbl4.setVisible(true);
				lbl5.setVisible(true);
				lbl6.setVisible(true);

				// con1.close();

			}
		} catch (Exception ex) {
		}

	}

	public void copy() throws IOException {
		File f1 = fc.getSelectedFile();
		tf.setText(f1.getAbsolutePath());
		in = new FileInputStream(f1.getAbsolutePath());
		Filename = f1.getAbsolutePath();
	}

	public static void main(String... d) throws ClassNotFoundException,
			SQLException {
		new CDRUpload();

	}
}