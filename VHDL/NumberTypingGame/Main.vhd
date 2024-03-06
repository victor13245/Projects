----------------------------------------------------------------------------------
-- Company: 
-- Engineer: 
-- 
-- Create Date: 11/30/2023 05:06:17 PM
-- Design Name: 
-- Module Name: Main - Behavioral
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

entity Main is
    Port ( clk : in STD_LOGIC;
           rst : in STD_LOGIC;
           start : in STD_LOGIC;
           check : in STD_LOGIC;
           input : in STD_LOGIC_VECTOR (7 downto 0);
           --scor : out STD_LOGIC_VECTOR (4 downto 0);
           an : out STD_LOGIC_VECTOR (3 downto 0);
           seg : out STD_LOGIC_VECTOR (7 downto 0);
           
           CS : out STD_LOGIC;
           SDIN : out STD_LOGIC;
           SCLK : out STD_LOGIC;
           DC : out STD_LOGIC;
           RES    : out STD_LOGIC;
           VBAT : out STD_LOGIC;
           VDD    : out STD_LOGIC );
end Main;

architecture Behavioral of Main is

signal reminder, reminder1 : std_logic_vector(5 downto 0) := "100000";
signal zero,finishS : std_logic := '0';
type tip_stare is (START1, DISPLAY, WAIT_ANSWER, CHECK_ANSWER, INCREMENT_SCORE, GAME_OVER);
signal stare : tip_stare := DISPLAY; --start
signal numar : STD_LOGIC_VECTOR (7 downto 0) := "00000000";
signal scoraux : STD_LOGIC_VECTOR (15 downto 0) := x"0000";
signal nivel : STD_LOGIC_VECTOR (4 downto 0) := "00000";
signal rtimer, rtimer1 : std_logic := '0';
signal timerEnable, timerEnable1, timerEnableAux : std_logic := '0';
signal valRom : std_logic_vector (3 downto 0) := "0000";
signal valFin : std_logic_vector (7 downto 0) := "00000000";
--signal rstRom : std_logic := '0';
signal enableRom : std_logic := '0';
signal addr : std_logic_vector (3 downto 0) := "0000";
signal data : std_logic_vector (15 downto 0);
signal checkD : std_logic := '0';
signal cs1, sdin1, sclk1, dc1, res1, vbat1, vdd1 : std_logic;
signal gameOver, show : std_logic := '0';

signal test : std_logic_vector(3 downto 0) := x"0";

signal nbn : std_logic_vector(47 downto 0) := x"000000000000";
begin

u2: entity work.Timer port map (clk, rtimer, timerEnable, nivel, reminder, zero);
u3: entity work.Timer port map (clk, rtimer1, timerEnable1, "11110", reminder1, finishS);
mem: entity work.ROM port map (rst, enableRom, addr, valFin);
deb: entity work.Debouncer port map (clk,rst,check,checkD);
ssd: entity work.displ7seg_basys port map (clk,rst,data,an,seg);
pmod: entity work.Pmod_driver port map (addr, clk, rst, gameOver, show, cs1, sdin1, sclk1, dc1, res1, vbat1, vdd1); 
rand: entity work.RandomGenerator port map (clk, test);

CS <= cs1;
SDIN <= sdin1;
SCLK <= sclk1;
DC <= dc1;
RES <= res1;
VBAT <= vbat1;
VDD <= vdd1;



process(Clk, Rst, start, zero, checkD, input, gameOver)
begin
    if RISING_EDGE(clk) then 
    
    if rst = '1' then
        addr <= x"0";
        stare <= START1; --start
        scoraux <= x"0000";
        rtimer <= '1';
        rtimer1 <= '1';
        enableRom <= '0';
        nivel <= "00000";
        timerEnable <= '0';
        gameOver <= '0';
        show <= '1';
    else
        rtimer <= '0';
        rtimer1 <= '0';
        case stare is
            when START1 => 
                rtimer1 <= '1';
                if (start = '1') then
                    timerEnable <= '0';
                    enableRom <= '1'; 
                    stare <= DISPLAY; 
                else 
                    stare <= START1; 
                end if;
            when DISPLAY =>
                show <= '1';
                timerEnable1 <= '1';
                numar <= valFin;--"00000001";
                if (finishS = '0')then 
                    stare <= DISPLAY; 
                else 
                    stare <= WAIT_ANSWER;
                    rtimer1 <= '1'; 
                    timerEnable1 <= '0';
                    show <= '0';
                end if;
                                    --    stare <= WAIT_ANSWER;         
            WHEN WAIT_ANSWER =>
                timerEnable <= '1';
                --enableRom <= '0'; 
                if (checkD = '1' or zero = '1') then -- RISING_EDGE(zero) or 
                    stare <= CHECK_ANSWER; 
                else 
                    --timerEnableAux <= '1';
                    stare <= WAIT_ANSWER;
                end if;
            when CHECK_ANSWER => if (input = numar ) then stare <= INCREMENT_SCORE; else stare <= GAME_OVER; end if;
            when INCREMENT_SCORE =>
                 if (checkD = '0') then
                    addr <= test;
                    nivel <= nivel + 1;
                    scoraux <= scoraux + reminder;
                    timerEnable <= '0';
                    --timerEnableAux <= '0';
                    rtimer <= '1';
                    --timerEnable <= '1';
                    stare <= DISPLAY;
                 else
                    stare <= INCREMENT_SCORE;
                 end if;
            when GAME_OVER => gameOver <= '1'; stare <= GAME_OVER; timerEnable <= '0'; 
        end case;
    end if;
    end if;
end process;  

--timerEnable <= timerEnableAux;

--scor <= scoraux;
--data <= "00000000000" & scoraux;
--data <= "0000000000" & reminder when gameOver = '0' else scoraux;
data <= scoraux when gameOver = '1' else "000" & nivel & "00" & reminder;
--data <= "000000000000000" & gameOver;


end Behavioral;