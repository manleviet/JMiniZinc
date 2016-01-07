package at.siemens.ct.jmz.writer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import at.siemens.ct.jmz.elements.output.OutputStatement;
import at.siemens.ct.jmz.elements.solving.SolvingStrategy;

public interface IModelWriter {

  /**
   * Returns a {@link String} of element declarations, each in a separate line.
   */
  @Override
  String toString();

  /**
   * Writes the element declarations to a {@link File}, each in a separate line.
   * 
   * @throws IOException
   */
  void toFile(File file) throws IOException;

  /**
   * Writes the element declarations to a temporary {@link File}, each in a separate line.
   * 
   * @return the generated temporary file.
   * @throws IOException
   */
  File toTempFile() throws IOException;

  /**
   * Writes the element declarations to an {@link OutputStream}, each in a separate line.
   */
  void toOutputStream(OutputStream outputStream);

  SolvingStrategy getSolvingStrategy();

  void setSolvingStrategy(SolvingStrategy solvingStrategy);

  OutputStatement getOutputStatement();

  void setOutputStatement(OutputStatement outputStatement);

}