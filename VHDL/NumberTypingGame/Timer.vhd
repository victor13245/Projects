----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date: 11/14/2023 12:59:45 PM
-- Design Name: 
-- Module Name: Timer - Behavioral
-- Project Name: 
-- Target Devices: 
-- Tool Versions: 
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
use IEEE.STD_LOGIC_ARITH.ALL;
use IEEE.STD_LOGIC_UNSIGNED.ALL;

-- Uncomment the following library declaration if using
-- arithmetic functions with Signed or Unsigned values
--use IEEE.NUMERIC_STD.ALL;

-- Uncomment the following library declaration if instantiating
-- any Xilinx leaf cells in this code.
--library UNISIM;
--use UNISIM.VComponents.all;

entity Timer is
    Port ( clk : in STD_LOGIC;
           rst : in STD_LOGIC;
           Enable : in STD_LOGIC;
           nivel : in STD_LOGIC_VECTOR(4 downto 0);
           timpFin : out STD_LOGIC_VECTOR(5 downto 0);
           timp_exp : out STD_LOGIC);
end Timer;

architecture Behavioral of Timer is

signal timp : STD_LOGIC_VECTOR(5 downto 0) := "100000" - ('0' & nivel); -- "100000" - ('0' & nivel)
signal aux : STD_LOGIC_VECTOR(5 downto 0);
signal timp_expir : STD_LOGIC := '0';
signal clk_D : STD_LOGIC := '0';

begin

nume: entity WORK.Clk_Div port map (clk,rst,clk_D);

process(clk_D, Enable, rst)
begin
    if(rst = '1') then
        timp <= "100000" - ('0' & nivel); -- "100000" - ('0' & nivel)
        timp_expir <= '0';
    else
        if Enable = '1' then
            if RISING_EDGE(clk_D) then
                if timp = "000000" then
                    timp_expir <= '1';
                else
                    timp <= timp - 1;
                    --timp <= aux;
                end if;
            end if;
        end if;
    end if;
end process;

timpFin <= timp;
timp_exp <= timp_expir;

end Behavioral;
