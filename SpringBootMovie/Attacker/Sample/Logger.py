from colorama import Fore

class Logger: 
    '''
    Visual banner for the script.
    '''
    
    def __init__(self) -> None:
        self.header = '''
        =========================================================================================
                     ___          _           ___           _   __  __         _     
                    / __|_ __ _ _(_)_ _  __ _| _ ) ___  ___| |_|  \/  |_____ _(_)___ 
                    \__ \ '_ \ '_| | ' \/ _` | _ \/ _ \/ _ \  _| |\/| / _ \ V / / -_)
                    |___/ .__/_| |_|_||_\__, |___/\___/\___/\__|_|  |_\___/\_/|_\___|
                        |_|             |___/                                                                                                
        =========================================================================================
        SpringBootMovie is an open-source Spring Boot web application that enables users to 
        browse movies, watch trailers, movie's details and keep updated to the latest movies 
        releases.
        
        - CVE-2022-28588: in SpringBootMovie <=1.2 when adding movie names, malicious code can be 
                          stored because there are no filtering parameters, resulting in stored XSS.
        - CVE-2022-29001: in SpringBootMovie <=1.2, the uploaded file suffix parameter is not 
                          filtered, resulting in arbitrary file upload vulnerability.

        This script contains a verbose exploit script used as a Proof of Concept (PoC) for both the 
        CVEs. More detailed information can be found in the README.md of the GitHub repository. 
        
        Contact: davide.arcolini@studenti.polito.it 
        '''
        
    def print_banner(self) -> None:
        print(self.header)
        return self
        
    def log(self, message: str) -> None:
        print(Fore.CYAN + '[+] ' + Fore.RESET + message)
        return self
    
    def success(self, message: str) -> None:
        print(Fore.GREEN + '[!] ' + message)
        return self
    
    def warning(self, message: str) -> None:
        print(Fore.RED + '[!] ' + message)
        return self
    
    def request(self, verb: str, endpoint: str) -> None:
        print('    ' + Fore.CYAN + f'{verb} {endpoint}')
        return self