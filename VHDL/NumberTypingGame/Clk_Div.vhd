library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.numeric_std.ALL;
  
entity Clk_Div is
port ( clk: in std_logic;
     reset: in std_logic;
    clock_out: out std_logic);
end Clk_Div;
  
architecture bhv of Clk_Div is
  
signal count: integer:=1;
signal tmp : std_logic := '0';
  
begin
  
    process(clk,reset)
    begin
        if(reset='1') then
            count<=1;
            tmp<='0';
            elsif(clk'event and clk='1') then
                count <=count+1;
            if (count = 50_000_000) then
                tmp <= NOT tmp;
                count <= 1;
            end if;
        end if;
        clock_out <= tmp;
    end process;
  
end bhv;