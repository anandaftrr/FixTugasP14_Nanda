
import java.util.InputMismatchException;
import java.util.Scanner;

import com.mysql.cj.protocol.Resultset;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;

public class Gaji implements PTABC {
	//static Scanner scanner;
	static Connection conn;
	String url = "jdbc:mysql://localhost:3306/pt_abc";
	
	public Integer NoPegawai;
	public Integer GajiPokok = 0; 
    public Integer JumlahHadir = 0;
    public Integer JumlahLibur = 0;
    public Integer TotalGaji = 0;
    public String NamaPegawai;
    public String Jabatan;
	public double Potongan = 0;


    Scanner Input = new Scanner (System.in);

    public void displaydatabase() throws SQLException {
		String text1 = "\nLihat Data";
		System.out.println(text1.toUpperCase());
						
		String sql ="SELECT * FROM gaji";
		conn = DriverManager.getConnection(url,"root","");
		Statement statement = conn.createStatement();
		ResultSet result = statement.executeQuery(sql);
		
		while(result.next()){
			System.out.print("\nNomor pegawai\t  : ");
            System.out.print(result.getInt("no_pegawai"));
            System.out.print("\nNama pegawai\t  : ");
            System.out.print(result.getString("nama_pegawai"));
            System.out.print("\nJabatan\t\t  : ");
            System.out.print(result.getString("jabatan"));
            System.out.print("\nJumlah hari masuk : ");
            System.out.print(result.getInt("jumlah_hadir"));
            System.out.print("\nTotal gaji\t  : ");
            System.out.print(result.getInt("total_gaji"));
            System.out.print("\n");
		}
	}
    
    public void insertdata() throws SQLException {
    	String text2 = "\nTambah Data";
		System.out.println(text2.toUpperCase());
		
    	try {
	        // NoPegawai
	    	System.out.print("Nomor Pegawai: ");
	        NoPegawai = Input.nextInt();
	        Input.nextLine();
	
	        // NamaPegawai
	        System.out.print("\nNama Pegawai: ");
	        NamaPegawai = Input.nextLine(); 
		        
	        // PilihJabatan
	        int pilihJabatan;
	        System.out.println("\n1. Direktur\n2. Manajer\n3. Administrasi\n4. Karyawan");
	        System.out.print("Pilih jabatan (1 - 4) : ");
	        pilihJabatan = Input.nextInt();
	        if (pilihJabatan == 1){
	            Jabatan = "Direktur";
	        }
	        else if (pilihJabatan == 2){
	            Jabatan = "Manajer";
	        }
	        else if (pilihJabatan == 3){
	            Jabatan = "Staff";
	        }
	        else if (pilihJabatan == 4){
	            Jabatan = "Satpam";
	        }
	        else{
	            System.out.println("Jabatan tidak tersedia");
	        }
	        System.out.println(Jabatan.toUpperCase());
	
	        if (Jabatan == "Direktur"){
	            GajiPokok = 10000000;
	        }
	        else if (Jabatan == "Manajer"){
	            GajiPokok = 8000000;
	        }
	        else if (Jabatan == "Staff"){
	            GajiPokok = 5000000;
	        }
	        else if (Jabatan == "Satpam"){
	            GajiPokok = 3000000;
	        }
	        else{
	            System.out.println("\nGaji Pokok Tidak Tersedia");
	        }
	        System.out.println("\nGaji Pokok : Rp" + GajiPokok);
	        
	        // JumlahHariMasuk
	        System.out.print("\nJumlah Libur (0 - 30): ");
	        JumlahLibur = Input.nextInt();
	        JumlahHadir = 30 - JumlahLibur;
	        System.out.println("Jumlah Hadir       : " + JumlahHadir);
	        
	        // Potongan
	        Potongan = JumlahLibur*25000;
	        System.out.println("\nPotongan: Rp" + (int) Potongan);
	        
	        // TotalGaji
	        TotalGaji = GajiPokok - (int) Potongan;
	        System.out.println("Total gaji: Rp" + TotalGaji);
	        System.out.println("");
	        
	        String sql = "INSERT INTO gaji (no_pegawai, nama_pegawai, jabatan, jumlah_hadir, total_gaji) VALUES ('"+NoPegawai+"','"+NamaPegawai+"','"+Jabatan+"','"+JumlahHadir+"','"+TotalGaji+"')";
	        conn = DriverManager.getConnection(url,"root","");
	        Statement statement = conn.createStatement();
	        statement.execute(sql);
	        System.out.println("Input Data Berhasil!");
    	}
    	catch (SQLException e) {
	        System.err.println("Input Data Error!");
	    } 
    	catch (InputMismatchException e) {
	    	System.err.println("Input Data Angka!");
	   	}
	} 
	public void ubahdata() throws SQLException{
		String text3 = "\nUbah Data";
		System.out.println(text3.toUpperCase());
		
		try {
            displaydatabase();
            System.out.print("\nMasukkan Nomor Pegawai Yang Diubah : ");
            Integer NoPegawai = Integer.parseInt(Input.nextLine());
            
            String sql = "SELECT * FROM gaji WHERE no_pegawai = " +NoPegawai;
            conn = DriverManager.getConnection(url,"root","");
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql);
            
            if(result.next()){
                
                System.out.print("Nama baru ["+result.getString("nama_pegawai")+"]\t: ");
                String NamaPegawai = Input.nextLine();
                   
                sql = "UPDATE gaji SET nama_pegawai='"+NamaPegawai+"' WHERE nama_pegawai='"+NoPegawai+"'";
                //System.out.println(sql);

                if(statement.executeUpdate(sql) > 0){
                    System.out.println("Berhasil Memperbaharui Data (Nomor "+NoPegawai+")");
                }
            }
            statement.close();        
        } 
		catch (SQLException e) {
        	System.err.println("Terjadi Kesalahan Pengeditan Data");
            System.err.println(e.getMessage());
        }
	}
	
	public void delete() {
		String text4 = "\nHapus Data";
		System.out.println(text4.toUpperCase());
		
		try{
	        displaydatabase();
	        System.out.print("\nNomor Pegawai Yang Akan Dihapus: ");
	        Integer NoPegawai= Integer.parseInt(Input.nextLine());
	        
	        String sql = "DELETE FROM gaji WHERE no_pegawai = "+ NoPegawai;
	        conn = DriverManager.getConnection(url,"root","");
	        Statement statement = conn.createStatement();
	        //ResultSet result = statement.executeQuery(sql);
	        
	        if(statement.executeUpdate(sql) > 0){
	            System.out.println("Berhasil Menghapus Data (Nomor "+NoPegawai+")");
	        }
	   }
		catch(SQLException e){
	        System.out.println("Terjadi Kesalahan Penghapusan Data");
	    }
        catch(Exception e){
            System.out.println("Masukan Data Yang Benar");
        }
	}
	
	public void searchdata() throws SQLException {
		String text5 = "\nCari Data";
		System.out.println(text5.toUpperCase());
				
		System.out.print("Nama Pegawai Yang Akan Dilihat : ");    
		String keyword = Input.nextLine();
		
		String sql = "SELECT * FROM gaji WHERE nama_pegawai LIKE '%"+keyword+"%'";
		conn = DriverManager.getConnection(url,"root","");
        Statement statement = conn.createStatement();
        ResultSet result = statement.executeQuery(sql); 
                
        while(result.next()){
        	System.out.print("\nNomor Pegawai\t  : ");
            System.out.print(result.getInt("no_pegawai"));
            System.out.print("\nNama Pegawai\t  : ");
            System.out.print(result.getString("nama_pegawai"));
            System.out.print("\nJabatan\t\t  : ");
            System.out.print(result.getString("jabatan"));
            System.out.print("\nJumlah Hari Masuk : ");
            System.out.print(result.getInt("jumlah_hadir"));
            System.out.print("\nTotal gaji\t  : ");
            System.out.print(result.getInt("total_gaji"));
            System.out.print("\n");
        }
	}   
}