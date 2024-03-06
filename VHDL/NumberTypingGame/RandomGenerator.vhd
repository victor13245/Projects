library ieee; 
use ieee.std_logic_1164.all;
use ieee.std_logic_unsigned.all;
entity RandomGenerator is 
port (
  clk    : in  std_logic;
  outp   : out std_logic_vector (3 downto 0));
end RandomGenerator;

architecture rtl of RandomGenerator is  

signal aux : std_logic_vector (31 downto 0) := x"45128546";

begin  

    process(clk)
    begin 
      if (rising_edge(clk)) then 
        aux <= aux + 15;
      end if; 
    end process; 
    
    outp <= aux(3 downto 0); 

end architecture rtl;