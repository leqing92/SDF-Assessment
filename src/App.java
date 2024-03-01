import java.io.Console;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class App {    
    public static List <String> pokemonDeck;
    public static Map<Integer, List<String>> pokemonStackMap = new HashMap<>();
    public static void main(String[] args) throws Exception {
        Boolean exit = false;
        String csvFilePath = "";
        //load file data to List<String)
        if(args.length > 0){
            csvFilePath = args[0];            
            FileService fileService = new FileService();
            pokemonDeck = fileService.ReadCSV(csvFilePath);
        }else{
            System.out.println("Please input Rush2.csv filepath");
        }
        //System.out.println(pokemonDeck);

        //load List<String> to Map
        for(int i = 0; i < pokemonDeck.size(); i++){
            pokemonStackMap.put(i + 1, Arrays.asList(pokemonDeck.get(i).split(",")));
        }        

        Console cons = System.console();

        while (!exit){
            printHeader();
            String input = cons.readLine("Enter you selection >").trim().toLowerCase();            
            switch (input) {
                case "q":
                    printExitMessage();
                    exit = true;
                    break;
                case "1":
                    input = cons.readLine("Display the list of unique Pokemon in stack (1 - 8) > ");
                    if(Integer.parseInt(input) >= 1 && Integer.parseInt(input) <= 8){
                        printUniquePokemonStack(Integer.parseInt(input));                          
                    }
                    else{
                        System.out.println("Please input stack in 1 - 8 only");                        
                    }
                    pressAnyKeyToContinue(); 
                    break;
                case "2":
                    input = cons.readLine("Search for the next occurence of 5 starts Pokemon in all stacks based on entered Pokemon > ");
                    printNext5StarsPokemon(input);
                    pressAnyKeyToContinue();
                    break;
                case "3":                                    
                    String pokemonStack = cons.readLine("Create a new Pokemon stack and save to a new file > ");
                    String filename = cons.readLine("Enter file name to save (e.g. path/filename.csv) > ");
                    savePokemonStack(pokemonStack, filename);
                    break;
                case "4":    
                    printPokemonCardCount();
                    pressAnyKeyToContinue();
                    break;
                default:
                    System.out.println("invalid input");
                    pressAnyKeyToContinue();
                    break;
            }            
            
        }

    }

    public static void clearConsole() throws IOException {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // Task 1
    public static void pressAnyKeyToContinue() {
        // your code here
        Console cons = System.console();
        cons.readLine("Press any key to continue...");        
    }

    // Task 1
    public static void printHeader() {
        System.out.println("Welcome to Pokemon Gaole Legend 4 Rush 2\n");
        //System.out.println("(1) View the list of Pokemon in the selected stack");
        System.out.println("(1) View unique list of Pokemon in the selected stack");
        System.out.println("(2) Find next 5 stars Pokemon occurence");
        System.out.println("(3) Create new Pokemon stack and save (append) to csv file");
        System.out.println("(4) Print distinct Pokemon and cards count");
        System.out.println("(q) to exit the program");
        // Task 1 - your code here
    }

    // Task 1
    public static void printExitMessage() {
        // Task 1 - your code here
        System.out.println("Thank you for using the program...");
        System.out.println("Hope to see you soon...");        
        
    }

    // Task 1
    public static void savePokemonStack(String pokemonStack, String filename) {
        // Task 1 - your code here
        FileService file = new FileService();
        file.writeAsCSV(pokemonStack, filename);
    }

    // Task 2
    public static void printUniquePokemonStack(Integer stack) {
        // Task 2 - your code here
        int count = 1;
        for(String entry : pokemonStackMap.get(stack).stream().distinct().collect(Collectors.toList())){            
            System.out.println(count + " ==> " + entry);
            count++;
        }
    }

    // Task 2
    public static void printNext5StarsPokemon(String enteredPokemon) {
        // Task 2 - your code here
        for (int i = 1; i <= 8; i++){            
            System.out.printf("Set %d\n", i);
            if(!pokemonStackMap.get(i).stream().anyMatch(s -> s.equals(enteredPokemon))){
                System.out.printf("%s is not found in this set.\n", enteredPokemon);
            }else if(pokemonStackMap.get(i).stream().anyMatch(s -> s.equals(enteredPokemon))){                
                List <String> eachStack = pokemonStackMap.get(i).stream().collect(Collectors.toList());
                int enteredPokemonIndex = eachStack.indexOf(enteredPokemon);
                int cardsToGo = -1;
                for(int j = 0; j < eachStack.size(); j++){
                    if(eachStack.get(j).startsWith("5*")){
                        cardsToGo = j;
                        break;                        
                    }
                }
                if((cardsToGo - enteredPokemonIndex + 1) <= 0){
                    System.out.println("No 5 stars Pokemon found subsequently in the stack.");
                }
                else{
                    System.out.printf("%s >>> %d cards to go.\n", eachStack.get(cardsToGo), cardsToGo - enteredPokemonIndex);
                }
            }
        }
    }
    

    // Task 2
    public static void printPokemonCardCount() {
        // Task 2 - your code here
        //get all card distribution
        Map <String, Integer> pokemonCardCountMap = new HashMap<>();
        for(int i = 0; i < pokemonDeck.size() ; i++){
            List <String> eachCard = Arrays.asList(pokemonDeck.get(i).split(","));
            for(String card : eachCard){
                pokemonCardCountMap.put(card, pokemonCardCountMap.getOrDefault(card, 0) + 1);
            }
        }
        //get top10 card 
        //https://stackoverflow.com/questions/109383/sort-a-mapkey-value-by-values
        Map<String, Integer> topTen = pokemonCardCountMap.entrySet().stream()
                                                        .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                                                        .limit(10)
                                                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        int count = 1;
        for(Map.Entry<String, Integer> entry : topTen.entrySet()){            
            System.out.printf("Pokemon %d : %s, Cards Count: %d\n", count, entry.getKey(), entry.getValue());
            count ++;
        }
    }

}
