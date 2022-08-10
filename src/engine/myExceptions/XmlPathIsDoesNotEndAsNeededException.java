package engine.myExceptions;

public class XmlPathIsDoesNotEndAsNeededException extends Exception{

    private final String EXCEPTION_MESSAGE = "The Xml path does not end with '.xml'";

    @Override
    public String getMessage() {return EXCEPTION_MESSAGE;}
}
