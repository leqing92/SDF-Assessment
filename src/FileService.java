import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileService {
    public List<String> ReadCSV(String fullPathFilename) {
        // Task 1 - your code here
        List <String> output = new ArrayList<>(); 
        File csvFile = new File(fullPathFilename);
                
        if(csvFile.exists()){
            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                String line;
        
                while ((line = br.readLine()) != null) {
                    output.add(line);
                }                    
                
            }catch(IOException e){
                System.out.println("IOException");
            }
        }
        else{
            System.out.println("File not exist");            
        }
        return output;
    }
        

    public void writeAsCSV(String pokemons, String fullPathFilename) {
        // Task 1 - your code here
        File csvFile = new File(fullPathFilename);
        // List <String> stackOfPokemon = new ArrayList<>();
        // stackOfPokemon.addAll(Arrays.asList(pokemons.split(",")));

        //create file if file no exist
        if(!csvFile.exists()){            
            try {
                csvFile.createNewFile();                
            } catch (IOException e) {                 
                e.printStackTrace();
            }          
            //write to file
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile))) { 
                bw.write(pokemons);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        //if file exist
        else if (csvFile.exists()){
            List <String> tempPokemonList = new ArrayList<>();
            tempPokemonList = this.ReadCSV(fullPathFilename);
            tempPokemonList.add(pokemons);
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile))) { 
                for(String line : tempPokemonList){
                    bw.write(line);
                    bw.newLine();                    
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
