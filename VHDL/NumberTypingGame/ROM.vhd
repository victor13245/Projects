----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date:    17:55:54 05/18/2022 
-- Design Name: 
-- Module Name:    RAM - Behavioral 
-- Project Name: 
-- Target Devices: 
-- Tool versions: 
-- Description: 
--
-- Dependencies: 
--
-- Revision: 
-- Revision 0.01 - File Created
-- Additional Comments: 
--
----------------------------------------------------------------------------------
library IEEE;
use IEEE.STD_LOGIC_1164.ALL;

-- Uncomment the following library declaration if using
-- arithmetic functions with Signed or Unsigned values
--use IEEE.NUMERIC_STD.ALL;

-- Uncomment the following library declaration if instantiating
-- any Xilinx primitives in this code.
--library UNISIM;
--use UNISIM.VComponents.all;

entity ROM is
    Port ( Rst : in  STD_LOGIC;
           Enable : in STD_LOGIC;
		   Addr_ROM : in STD_LOGIC_VECTOR(3 downto 0);
           D_RAM : out  STD_LOGIC_VECTOR(7 downto 0));
end ROM;

architecture Behavioral of ROM is

type MEMORIE is array (0 to 15) of STD_LOGIC_VECTOR (7 downto 0);
signal M : MEMORIE := (x"00",x"01",x"02",x"03",x"04",x"05",x"06",x"07",x"08",x"09",x"0A",x"0B",x"0C",x"0D",x"0E",x"0F");

begin
process (M,Addr_ROM,Rst,Enable)
begin
if (Rst = '1') then D_RAM <= x"00";
else
    if Enable = '1' then
	case Addr_ROM is
		when "0000" => D_RAM <= M(0);
		when "0001" => D_RAM <= M(1);
		when "0010" => D_RAM <= M(2);
		when "0011" => D_RAM <= M(3);
		when "0100" => D_RAM <= M(4);
		when "0101" => D_RAM <= M(5);
		when "0110" => D_RAM <= M(6);
		when "0111" => D_RAM <= M(7);
		when "1000" => D_RAM <= M(8);
		when "1001" => D_RAM <= M(9);
		when "1010" => D_RAM <= M(10);
		when "1011" => D_RAM <= M(11);
		when "1100" => D_RAM <= M(12);
		when "1101" => D_RAM <= M(13);
		when "1110" => D_RAM <= M(14);
		when "1111" => D_RAM <= M(15);
		when others => D_RAM <= x"FF";
	end case;
	end if;
end if;
end process;
end Behavioral;
