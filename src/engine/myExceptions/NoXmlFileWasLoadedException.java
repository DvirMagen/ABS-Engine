package engine.myExceptions;

public class NoXmlFileWasLoadedException extends Exception{

    private final String EXCEPTION_MESSAGE = "\nNO XML FILE WAS LOADED!\nPleas load XML file before you choose any other option\n";

    public NoXmlFileWasLoadedException(){}

    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}

}
