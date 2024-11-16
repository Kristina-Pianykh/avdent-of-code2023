class InvalidPipeCharException extends Exception {
  public InvalidPipeCharException(char ch) {
    super("Could not instantiate a Pipe instance: invalid character: " + ch);
  }
}
