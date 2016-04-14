/**
 * Created by sophea on 13/04/2016.
 */
public class test {
    public static void main(String[] args) {
        HackAssembler assembler = new HackAssembler("shit.txt");
        System.out.println(assembler.symbolTable.entrySet());
    }
}
